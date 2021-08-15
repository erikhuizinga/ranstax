package com.github.erikhuizinga.ranstax

import kotlin.test.Test
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
    fun decrementMultipleStacksDecrementsOneStack() {
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
}
