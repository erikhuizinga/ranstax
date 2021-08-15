package com.github.erikhuizinga.ranstax

/**
 * A named stack of a known number of items.
 *
 * @property size The size of this stack. Cannot be negative.
 * @property name The name of this stack. Cannot be blank or empty.
 */
sealed interface Stack {
    val name: String
    val size: Int
}

/**
 * Create a named [Stack] of a known number of items.
 *
 * @param name The name of the stack.
 * @param size The size of the stack.
 * @throws IllegalStateException If:
 * - [size] is not positive.
 * - [name] is blank or empty.
 */
fun Stack(name: String, size: Int): Stack {
    require(size > 0) { "size must be positive but was $size" }
    require(name.isNotBlank()) { "name must not be blank or empty" }
    return LeafStack(name, size)
}

/**
 * Create a named [Stack] from multiple stacks.
 *
 * @param stacks The stacks to form a stack with.
 * @throws IllegalStateException If [stacks] is empty.
 */
fun Stack(vararg stacks: Stack): Stack = Stack(stacks.toList())

/**
 * Create a named [Stack] from multiple stacks.
 *
 * @param stacks The stacks to form a stack with.
 * @throws IllegalStateException If [stacks] is empty.
 */
fun Stack(stacks: Collection<Stack>): Stack = when (stacks.size) {
    0 -> throw IllegalArgumentException("stacks must contain at least one stack")
    1 -> stacks.single()
    else -> NodeStack(stacks)
}

internal data class LeafStack(override val name: String = "", override val size: Int = 0) : Stack

internal data class NodeStack(val stacks: Collection<Stack>) : Stack {
    constructor(vararg stacks: Stack) : this(stacks.toList())

    override val name = stacks.map(Stack::name).toString()
    override val size = stacks.sumOf(Stack::size)
}
