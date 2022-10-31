package com.github.erikhuizinga.ranstax.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RanstaxState(
    val allStacks: Map<Stack, Boolean> = emptyMap(),
    val drawnStackNames: List<List<String>> = emptyList(),
    @Transient
    val isEditing: Boolean = false,
) {
    val stacks = allStacks.filterValues { !it }.keys
    val stacksBeingEdited = allStacks.filterValues { it }.keys
    val hasStacks = allStacks.isNotEmpty()
    val totalStackSize = allStacks.keys.sumOf { it.size }
    val areAllStacksEmpty = totalStackSize == 0
    val isDrawButtonEnabled = !areAllStacksEmpty && stacksBeingEdited.isEmpty()
}
