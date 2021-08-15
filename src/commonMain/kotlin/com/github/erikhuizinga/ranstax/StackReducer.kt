package com.github.erikhuizinga.ranstax

import kotlin.random.Random

internal fun interface StackReducer<S : Stack> {
    operator fun invoke(stack: S): S
}

internal val defaultStackReducer = StackReducer<Stack> { stack ->
    when (stack) {
        is NodeStack -> nodeStackReducer(stack)
        is LeafStack -> leafStackReducer(stack)
    }
}

internal val leafStackReducer = StackReducer<LeafStack> { leafStack ->
    check(leafStack.size > 0) { "Cannot decrement a stack of size ${leafStack.size}" }
    leafStack.copy(size = leafStack.size - 1)
}

internal val nodeStackReducer = nodeStackReducer()

internal fun nodeStackReducer(random: Random = Random.Default): StackReducer<NodeStack> =
    StackReducer { nodeStack ->
        check(nodeStack.size > 0) { "Cannot decrement a stack of size ${nodeStack.size}" }
        val stacks = nodeStack.stacks
        val stackToReduce = stacks.flatMap { stack -> List(stack.size) { stack } }.random(random)
        val reducedStack = defaultStackReducer(stackToReduce)
        NodeStack(stacks - stackToReduce + reducedStack)
    }
