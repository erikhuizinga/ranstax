package com.github.erikhuizinga.ranstax

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class StackReducerTest {
    @Test
    fun emptyLeafStackReductionThrowsISE() {
        val leafStack = LeafStack()

        val illegalStateException = assertFailsWith<IllegalStateException> {
            leafStackReducer(leafStack)
        }
        assertEquals(
            expected = "Cannot decrement a stack of size ${leafStack.size}",
            actual = illegalStateException.message
        )
    }

    @Test
    fun emptyNodeStackReductionThrowsISE() {
        val nodeStack = NodeStack()

        val illegalStateException = assertFailsWith<IllegalStateException> {
            nodeStackReducer(nodeStack)
        }
        assertEquals(
            expected = "Cannot decrement a stack of size ${nodeStack.size}",
            actual = illegalStateException.message
        )
    }

    @Test
    fun nonEmptyLeafStackReductionDecrementsSize() {
        val size = 1
        val leafStack = LeafStack(size = 1)
        val expected = LeafStack(size = size - 1)

        val actual = leafStackReducer(leafStack)

        assertEquals(expected, actual)
    }

    @Test
    fun nodeStackWithSingletonStack_Reduction_DecrementsBothStacksAndKeepsTheirNames() {
        val name = "StackyMcStackFace"
        val preStack = Stack(name = name, size = 1)
        val preNodeStack = NodeStack(preStack)
        val size = preNodeStack.size

        val postNodeStack = nodeStackReducer(preNodeStack)

        val postStack = postNodeStack.stacks.single()
        assertEquals(expected = name, actual = postStack.name)
        assertEquals(expected = preNodeStack.name, actual = postNodeStack.name)
        assertEquals(expected = size - 1, actual = postNodeStack.size)
        assertEquals(expected = size - 1, actual = postStack.size)
    }

    @Test
    fun multipleStacks_Reduction_DecrementsOneStack() {
        val nStacks = 5
        val preStacks = List(nStacks) { Stack(name = it.toString(), size = it + 1) }
        var nodeStack = NodeStack(preStacks)
        val size = nodeStack.size

        nodeStack = nodeStackReducer(nodeStack)

        val postStacks = nodeStack.stacks
        val thePostStack = postStacks.minus(preStacks).single()
        val thePreStack = preStacks.single { it.name == thePostStack.name }
        assertEquals(expected = nStacks - 1, actual = postStacks.intersect(preStacks).size)
        assertEquals(expected = thePreStack.size - 1, actual = thePostStack.size)
        assertEquals(expected = size - 1, actual = nodeStack.size)
    }

    @Test
    fun multipleStacksWithControlledRandom_Reduction_DecrementsExpectedStacks() {
        val random: Random = object : Random() {
            override fun nextBits(bitCount: Int) = TODO()
            override fun nextInt(until: Int) = 0
        }
        val reducer = nodeStackReducer(random)
        val nStacks = 50
        val stacks = List(nStacks) {
            val size = it + 1
            Stack(name = size.toString(), size = size)
        }
        var stack = NodeStack(stacks)
        val expected = (1..nStacks).flatMap { n -> List(n) { n.toString() } }

        val actual = List(stack.size) {
            val preStacks = stack.stacks
            stack = reducer(stack)
            stack.stacks.minus(preStacks).single()
        }.map(Stack::name)

        assertContentEquals(expected, actual)
        assertEquals(expected = 0, actual = stack.size)
    }
}
