package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExistingStackValidatorTest {
    private companion object {
        private var fakeCalls = 0

        private fun createFakeNewStackValidator(
            stackValidation: StackValidation,
        ) = NewStackValidator {
            fakeCalls++
            stackValidation
        }
    }

    @BeforeTest
    fun beforeTest() {
        fakeCalls = 0
    }

    @Test
    fun givenANewStackValidatorThatDoesNotReturnNameExists_WhenValidated_ThenItReturnsTheValidation() {
        val stack = Stack("name", 1)
        StackValidation
            .values()
            .filterNot { it == StackValidation.NameExists }
            .forEach { expectedStackValidation ->
                val newStackValidator = createFakeNewStackValidator(expectedStackValidation)
                val existingStackValidator = ExistingStackValidator(
                    RanstaxState(),
                    stack,
                    newStackValidator,
                )

                val actual = existingStackValidator(stack)

                assertEquals(
                    expectedStackValidation,
                    actual
                )
                assertEquals(
                    1,
                    fakeCalls
                )
                fakeCalls = 0
            }
    }

    @Test
    fun givenANewStackValidatorThatReturnsNameExistsForThisStack_WhenValidated_ThenItReturnsValid() {
        val thisStack = Stack("name", 1)
        val newStackValidator = createFakeNewStackValidator(StackValidation.NameExists)
        val existingStackValidator = ExistingStackValidator(
            RanstaxState(),
            thisStack,
            newStackValidator,
        )

        val actual = existingStackValidator(thisStack)

        assertEquals(
            StackValidation.Valid,
            actual
        )
        assertEquals(
            1,
            fakeCalls
        )
    }

    @Test
    fun givenANewStackValidatorThatReturnsNameExistsForAnotherStack_WhenValidated_ThenItReturnsNameExists() {
        val thisStack = Stack("name", 1)
        val anotherStack = Stack("another", 1)
        val newStackValidator = createFakeNewStackValidator(StackValidation.NameExists)
        val existingStackValidator = ExistingStackValidator(
            RanstaxState(),
            thisStack,
            newStackValidator,
        )

        val actual = existingStackValidator(anotherStack)

        assertEquals(
            StackValidation.NameExists,
            actual
        )
        assertEquals(
            1,
            fakeCalls
        )
    }
}