package com.github.erikhuizinga.ranstax.data

import kotlin.test.Test
import kotlin.test.assertEquals

class StackTest {
    @Test
    fun givenANameWithoutLeadingAndTrailingWhiteSpace_WhenTrimNameIsInvoked_ThenAnEqualStackIsReturned() {
        val stack = Stack(name = "name", size = 0)

        assertEquals(
            stack,
            stack.trimName()
        )
    }

    @Test
    fun givenANameWithLeadingOrTrailingWhiteSpace_WhenTrimNameIsInvoked_ThenAStackIsReturnedWithTrimmedName() {
        val name = "name"
        val expected = Stack(name = name, size = 0)

        assertEquals(
            expected,
            expected.copy(name = " $name").trimName()
        )
        assertEquals(
            expected,
            expected.copy(name = "$name ").trimName()
        )
    }
}