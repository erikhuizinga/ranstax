package com.github.erikhuizinga.ranstax

/**
 * @constructor Throws an [IllegalArgumentException] is size is not positive.
 * @param size Must be positive.
 * @property size The size of this stack. Cannot be negative.
 */
data class Stack(val size: Int) {
    init {
        require(size > 0)
    }
}
