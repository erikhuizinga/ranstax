package com.github.erikhuizinga.ranstax

/**
 * @throws IllegalArgumentException If [stacks] is empty.
 * @param stacks Must not be empty.
 * @property stacks The [Stacks][Stack] in this session.
 */
class Session(var stacks: Collection<Stack>) {
    constructor(vararg stacks: Stack) : this(stacks.toList())

    init {
        require(stacks.isNotEmpty()) { "stacks must not be empty" }
    }

    private var stack = NodeStack(stacks)

    /**
     * Take an item from a random stack.
     * This function updates [stacks].
     *
     * @return The stack from which the item was taken.
     */
    fun takeFromRandomStack(): Stack {
        val preStacks = stacks
        stack = nodeStackReducer(stack)
        stacks = stack.stacks
        return stacks.minus(preStacks).single()
    }
}
