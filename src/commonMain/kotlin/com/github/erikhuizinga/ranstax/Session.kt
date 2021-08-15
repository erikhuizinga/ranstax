package com.github.erikhuizinga.ranstax

/**
 * @constructor Throws an [IllegalArgumentException] if [stacks] is empty.
 * @param stacks Must not be empty.
 * @property stacks The [Stacks][Stack] in this session.
 */
class Session(val stacks: List<Stack>) {
    constructor(vararg stacks: Stack) : this(stacks.toList())

    init {
        require(stacks.isNotEmpty())
    }
}
