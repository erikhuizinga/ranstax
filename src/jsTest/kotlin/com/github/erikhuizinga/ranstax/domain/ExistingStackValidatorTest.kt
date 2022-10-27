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
        StackValidation
            .values()
            .filterNot { it == StackValidation.NameExists }
            .forEach { expectedStackValidation ->
                val newStackValidator = createFakeNewStackValidator(expectedStackValidation)
                val existingStackValidator =
                    ExistingStackValidator(RanstaxState(), newStackValidator)

                val actual = existingStackValidator(Stack("name", 1))

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
    fun givenANewStackValidatorThatReturnsNameExists_WhenValidated_ThenItReturnsValid() {
        val newStackValidator = createFakeNewStackValidator(StackValidation.NameExists)
        val existingStackValidator = ExistingStackValidator(RanstaxState(), newStackValidator)

        val actual = existingStackValidator(Stack("name", 1))

        assertEquals(
            StackValidation.Valid,
            actual
        )
        assertEquals(
            1,
            fakeCalls
        )
    }
}