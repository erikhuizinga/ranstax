package com.github.erikhuizinga.ranstax.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RanstaxState(
    val stacks: List<Stack> = emptyList(),
    val stacksBeingEdited: List<Stack> = emptyList(),
    val drawnStackNames: List<List<String>> = emptyList(),
    @Transient
    val isEditing: Boolean = false,
) {
    init {
        require(stacks.containsAll(stacksBeingEdited)) {
            "stacks (${stacks.joinToString()}) must contain all " + "stacksBeingEdited (${stacksBeingEdited.joinToString()})." + " Stacks not in stacks: " + (stacksBeingEdited - stacks.toSet()).joinToString()
        }
    }

    val hasStacks = stacks.isNotEmpty()
    val totalStackSize = stacks.sumOf { it.size }
    val areAllStacksEmpty = totalStackSize == 0
    val isDrawButtonEnabled = !areAllStacksEmpty && stacksBeingEdited.isEmpty()
}
