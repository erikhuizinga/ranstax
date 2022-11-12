package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.Stack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class StackValidatorTest {
    @Test
    fun emptyOrBlankNameValidatesToNameBlank() {
        val stack = Stack("", 0)
        val stackValidator = StackValidator(emptySet())

        assertEquals(
            StackValidation.NameBlank,
            stackValidator(stack)
        )

        val stack1 = Stack(" ", 0)
        assertEquals(
            StackValidation.NameBlank,
            stackValidator(stack1)
        )
    }

    @Test
    fun zeroSizeValidatesToSizeMustBeAtLeastOne() {
        val stackValidator = StackValidator(emptySet())
        val stack = Stack("name", 0)

        assertEquals(
            StackValidation.SizeMustBeAtLeastOne,
            stackValidator(stack)
        )
    }

    @Test
    fun validStackValidatesToValid() {
        val stackValidator = StackValidator(emptySet())
        val stack = Stack("name", 1)

        assertEquals(
            StackValidation.Valid,
            stackValidator(stack)
        )
    }

    @Test
    fun existingStackNameValidatesToNameExists() {
        val stack0 = Stack("name", 1)
        val stack1 = Stack("name", 2)
        val stack2 = Stack("another name", 1)
        val stackValidator = StackValidator(setOf(stack0))

        assertEquals(
            StackValidation.NameExists,
            stackValidator(stack0)
        )
        assertEquals(
            StackValidation.NameExists,
            stackValidator(stack1)
        )
        assertNotEquals(
            StackValidation.NameExists,
            stackValidator(stack2)
        )
    }
}