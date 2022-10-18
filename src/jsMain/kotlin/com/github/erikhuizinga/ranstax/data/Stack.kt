package com.github.erikhuizinga.ranstax.data

import kotlinx.serialization.Serializable

@Serializable
data class Stack(
    val name: String,
    val size: Int,
) {
    /**
     * `true` if this stack is valid; `false` otherwise.
     * It is valid if its [name] is not empty and its [size] is not negative.
     */
    val isValid get() = name.isNotBlank() && size >= 0

    fun trimName() = copy(name = name.trim())
}