package com.github.erikhuizinga.ranstax.data

import kotlinx.serialization.Serializable

@Serializable
data class Stack(
    val name: String,
    val size: Int,
) {
    fun trimName() = copy(name = name.trim())
}
