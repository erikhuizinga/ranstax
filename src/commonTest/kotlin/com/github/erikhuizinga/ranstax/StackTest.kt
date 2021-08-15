package com.github.erikhuizinga.ranstax

import kotlin.test.*

class StackTest {
    @Test
    fun singleStackCreationWith0SizeThrowsIAE() {
        val size = 0
        val name = ""

        val illegalArgumentException = assertFailsWith<IllegalArgumentException> {
            Stack(name, size)
        }
        assertEquals(
            expected = "size must be positive but was $size",
            actual = illegalArgumentException.message
        )
    }

    @Test
    fun multiStackCreationWith0StacksThrowsIAE() {
        val stackList = emptyList<Stack>()
        val stackArray = stackList.toTypedArray()

        val illegalArgumentException1 = assertFailsWith<IllegalArgumentException> {
            Stack(stackList)
        }
        assertEquals(
            expected = "stacks must contain at least one stack",
            actual = illegalArgumentException1.message
        )

        val illegalArgumentException2 = assertFailsWith<IllegalArgumentException> {
            Stack(*stackArray)
        }
        assertEquals(
            expected = "stacks must contain at least one stack",
            actual = illegalArgumentException2.message
        )
    }

    @Test
    fun singleStackCreationWithEmptyOrBlankNameThrowsIAE() {
        val size = 1
        val emptyName = ""
        val blankName = " "

        listOf(emptyName, blankName).forEach { name ->
            val illegalArgumentException = assertFailsWith<IllegalArgumentException> {
                Stack(name, size)
            }
            assertEquals(
                expected = "name must not be blank or empty",
                actual = illegalArgumentException.message
            )
        }
    }

    @Test
    fun singleStackCreationIsALeafStack() {
        assertIs<LeafStack>(Stack("StackyMcStackFace", Int.MAX_VALUE))
    }

    @Test
    fun multiStackCreationWith1StackIsTheSameStack() {
        val expected = LeafStack()

        val actual1 = Stack(expected)
        val actual2 = Stack(listOf(expected))

        assertSame(expected, actual1)
        assertSame(expected, actual2)
    }

    @Test
    fun multiStackCreationWithMultipleStacksIsANodeStack() {
        val stacks = List(5) { LeafStack() }

        assertIs<NodeStack>(Stack(stacks))
        assertIs<NodeStack>(Stack(*stacks.toTypedArray()))
    }
}
