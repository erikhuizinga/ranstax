package com.github.erikhuizinga.ranstax.data

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StackTest {
    @Test
    fun givenAnEmptyName_ThenIsValidIsFalse() {
        assertFalse(Stack(name = "", size = 0).isValid)
    }

    @Test
    fun givenABlankName_ThenIsValidIsFalse() {
        assertFalse(Stack(name = " ", size = 0).isValid)
    }

    @Test
    fun givenANegativeSize_ThenIsValidIsFalse() {
        assertFalse(Stack(name = "name", size = -1).isValid)
    }

    @Test
    fun givenANonBlankNameAndNonNegativeSize_ThenIsValidIsTrue() {
        assertTrue(Stack(name = "name", size = 0).isValid)
    }

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