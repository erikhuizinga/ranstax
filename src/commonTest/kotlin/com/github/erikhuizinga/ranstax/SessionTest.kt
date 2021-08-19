package com.github.erikhuizinga.ranstax

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class SessionTest {
    @Test
    fun sessionConstructorWithoutStacksThrowsIAE() {
        val expected = "stacks must not be empty"
        val illegalArgumentException1 = assertFailsWith<IllegalArgumentException> { Session() }
        assertEquals(expected, actual = illegalArgumentException1.message)
        val illegalArgumentException2 = assertFailsWith<IllegalArgumentException> {
            Session(emptyList())
        }
        assertEquals(expected, actual = illegalArgumentException2.message)
    }

    @Test
    fun sessionWithSingleStack_TakingReturnsSameStackWithDecrementedSize() {
        val size = 1
        val stack = LeafStack(size = size)
        val session = Session(stack)
        val expected = stack.copy(size = size - 1)

        val actual = session.takeFromRandomStack()

        assertEquals(expected, actual)
    }

    @Test
    fun sessionWithMultipleStacks_RepeatedlyTakingReturnsAllStacksRandomly() {
        fun List<Stack>.sort() =
            sortedWith(compareBy(Stack::name).thenByDescending(Stack::size))

        val nStacks = 50

        val stacks = List(nStacks) {
            val size = it + 1
            Stack(name = size.toString(), size = size)
        }
        val totalSize = stacks.sumOf(Stack::size)
        val session = Session(stacks)
        val expected = stacks.flatMap { stack ->
            sequence {
                var theStack = stack
                repeat(theStack.size) {
                    theStack = defaultStackReducer(theStack)
                    yield(theStack)
                }
            }
        }.sort()

        val actual = List(totalSize) { session.takeFromRandomStack() }.sort()

        assertContentEquals(expected, actual)
    }
}
