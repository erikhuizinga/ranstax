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
    @Transient
    private val idGenerator = iterator {
        var id = stateStacks.maxOfOrNull { it.id } ?: -1
        while (true) yield(++id)
    }

    @Transient
    val stacks: List<Stack> = stateStacks.map { it.stack }

    @Transient
    val stacksBeingEdited = stateStacks.mapNotNull { stateStack ->
        stateStack.stack.takeIf { stateStack.isBeingEdited }
    }

    @Transient
    val hasStacks = stateStacks.isNotEmpty()

    @Transient
    val totalStackSize = stateStacks.sumOf { it.stack.size }

    @Transient
    val isEmpty = totalStackSize == 0

    @Transient
    val canDraw = !isEmpty && stacksBeingEdited.isEmpty()

    operator fun plus(newStack: Stack): RanstaxState = copy(
        stateStacks = stateStacks + StateStack(idGenerator.next(), newStack)
    )

    fun replace(id: Int, stack: Stack) = replace(id) {
        copy(stack = stack)
    }

    fun replace(id: Int, isBeingEdited: Boolean) = replace(id) {
        copy(isBeingEdited = isBeingEdited)
    }

    fun replace(id: Int, stack: Stack, isBeingEdited: Boolean) = replace(id) {
        copy(stack = stack, isBeingEdited = isBeingEdited)
    }

    private fun replace(id: Int, transform: StateStack.() -> StateStack) = copy(
        stateStacks = stateStacks.map { if (it.id == id) it.transform() else it }
    )
}

@Serializable
data class StateStack(val id: Int, val stack: Stack, val isBeingEdited: Boolean = false)
