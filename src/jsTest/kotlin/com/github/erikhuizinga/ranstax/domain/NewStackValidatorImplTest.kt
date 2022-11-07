package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.data.StateStack
import kotlin.test.Test
import kotlin.test.assertEquals

class NewStackValidatorImplTest {
    @Test
    fun givenAnEmptyName_WhenValidated_ThenNameBlankIsReturned() {
        val newStackValidator = NewStackValidatorImpl(RanstaxState())
        val stack = Stack(name = "", size = 0)

        assertEquals(
            StackValidation.NameBlank,
            newStackValidator(stack)
        )
    }

    @Test
    fun givenABlankName_WhenValidated_ThenNameBlankIsReturned() {
        val newStackValidator = NewStackValidatorImpl(RanstaxState())
        val stack = Stack(name = " ", size = 0)

        assertEquals(
            StackValidation.NameBlank,
            newStackValidator(stack)
        )
    }

    @Test
    fun givenZeroSize_WhenValidated_ThenSizeMustBeAtLeastOneIsReturned() {
        val newStackValidator = NewStackValidatorImpl(RanstaxState())
        val stack = Stack(name = "name", size = 0)

        assertEquals(
            StackValidation.SizeMustBeAtLeastOne,
            newStackValidator(stack)
        )
    }

    @Test
    fun givenAValidStackThatExists_WhenValidated_ThenNameExistsIsReturned() {
        val stack0 = Stack(name = "name", size = 1)
        val stack1 = Stack(name = " name ", size = 1)
        val stateStacks = listOf(stack0, stack1).mapIndexed { index, stack ->
            StateStack(index, stack)
        }
        val ranstaxState = RanstaxState(stateStacks = stateStacks)
        val stackValidator = NewStackValidatorImpl(ranstaxState)

        assertEquals(
            StackValidation.NameExists,
            stackValidator(stack0)
        )
        assertEquals(
            StackValidation.NameExists,
            stackValidator(stack1)
        )
    }

    @Test
    fun givenAValidStackThatDoesNotExists_WhenValidated_ThenValidIsReturned() {
        val stack = Stack(name = "name", size = 1)
        val stackValidator = NewStackValidatorImpl(RanstaxState())

        assertEquals(
            StackValidation.Valid,
            stackValidator(stack)
        )
    }

    @Test
    fun givenValidStacksThatDoNotExistingWithOptionalBlankPadding_WhenValidated_ThenValidIsReturned() {
        val ranstaxState = RanstaxState()
        val stack0 = Stack(name = "name", size = 1)
        val stack1 = Stack(name = " name ", size = 1)
        val newStackValidator = NewStackValidatorImpl(ranstaxState)

        assertEquals(
            StackValidation.Valid,
            newStackValidator(stack0)
        )
        assertEquals(
            StackValidation.Valid,
            newStackValidator(stack1)
        )
    }

    @Test
    fun givenExistingNameWithOptionalBlankPaddingNotBeingEdited_WhenValidated_ThenNameExistsIsReturned() {
        val name = "name"
        val paddedName = " name "
        val existingStack = Stack(name = name, size = 1)
        val ranstaxState = RanstaxState(stateStacks = listOf(StateStack(0, existingStack)))
        val newStackValidator = NewStackValidatorImpl(ranstaxState)
        val newStack = Stack(name = paddedName, size = 1)

        assertEquals(
            StackValidation.NameExists,
            newStackValidator(existingStack)
        )
        assertEquals(
            StackValidation.NameExists,
            newStackValidator(newStack)
        )
    }

    @Test
    fun givenExistingNameWithOptionalBlankPaddingBeingEdited_WhenValidated_ThenValidIsReturned() {
        val name = "name"
        val paddedName = " name "
        val stack0 = Stack(name = name, size = 1)
        val ranstaxState = RanstaxState(stateStacks = listOf(StateStack(0, stack0, true)))
        val existingNameValidator = NewStackValidatorImpl(ranstaxState)
        val stack1 = Stack(name = paddedName, size = 1)

        assertEquals(
            StackValidation.Valid,
            existingNameValidator(stack0)
        )
        assertEquals(
            StackValidation.Valid,
            existingNameValidator(stack1)
        )
    }
}