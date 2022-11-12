package com.github.erikhuizinga.ranstax.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RanstaxState(
    val stateStacks: List<StateStack> = emptyList(),
    val drawnStackNames: List<List<String>> = emptyList(),
    /** `true` while the user is about to type anything, anywhere; `false` otherwise. */
    @Transient
    val isEditing: Boolean = false,
) {
    private val idGenerator by lazy {
        iterator {
            var id = stateStacks.maxOfOrNull { it.id } ?: -1
            while (true) yield(++id)
        }
    }

    val stacks: List<Stack> by lazy { stateStacks.map { it.stack } }

    val stacksBeingEdited by lazy {
        stateStacks.mapNotNull { stateStack ->
            stateStack.stack.takeIf { stateStack.isBeingEdited }
        }
    }

    val stacksNotBeingEdited by lazy { stacks - stacksBeingEdited.toSet() }
    val hasStacks by lazy { stateStacks.isNotEmpty() }
    val totalStackSize by lazy { stateStacks.sumOf { it.stack.size } }
    val isEmpty by lazy { totalStackSize == 0 }
    val canDraw by lazy { !isEmpty && stacksBeingEdited.isEmpty() }

    operator fun plus(newStack: Stack): RanstaxState = copy(
        stateStacks = stateStacks + StateStack(idGenerator.next(), newStack)
    )

    operator fun minus(stack: Stack): RanstaxState = copy(
        stateStacks = stateStacks.filterNot { it.stack == stack }
    )

    fun replace(id: Int, stack: Stack) = replace(id) {
        copy(stack = stack)
    }

    fun replace(id: Int, isBeingEdited: Boolean) = replace(id) {
        copy(isBeingEdited = isBeingEdited)
    }

    private fun replace(id: Int, transform: StateStack.() -> StateStack) = copy(
        stateStacks = stateStacks.map { if (it.id == id) it.transform() else it }
    )
}

@Serializable
data class StateStack(val id: Int, val stack: Stack, val isBeingEdited: Boolean = false)
