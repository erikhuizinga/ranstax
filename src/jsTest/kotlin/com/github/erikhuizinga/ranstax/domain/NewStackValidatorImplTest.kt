package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
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

//    @Test
//    fun givenANotNewStackWithExistingName_WhenValidated_ThenExistingName() {
//        val stack0 = Stack(name = "name0", size = 1)
//        val stack1 = Stack(name = "name1", size = 2)
//        val ranstaxState = RanstaxState(
//            stacks = listOf(stack0, stack1),
//            stacksBeingEdited = listOf(stack0),
//        )
//        val stackValidator = NewStackValidator(ranstaxState)
//        val stack2 = stack0.copy(name = stack1.name)
//
//        assertEquals(
//            StackValidation.ExistingName,
//            stackValidator(stack2)
//        )
//    }

//    @Test
//    fun givenAValidStackThatIsNotNew_WhenValidated_ThenValid() {
//        val stack = Stack(name = "name", size = 1)
//        val ranstaxState = RanstaxState(stacks = listOf(stack), stacksBeingEdited = listOf(stack))
//        val stackValidator = NewStackValidator(ranstaxState)
//
//        assertEquals(
//            StackValidation.Valid,
//            stackValidator(stack)
//        )
//    }

    @Test
    fun givenAValidStackThatExists_WhenValidated_ThenNameExistsIsReturned() {
        val stack0 = Stack("name", size = 1)
        val stack1 = Stack(" name ", size = 1)
        val ranstaxState = RanstaxState(stacks = listOf(stack0, stack1))
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
        val stack = Stack("name", size = 1)
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
        val existingStack = Stack(name, 1)
        val ranstaxState = RanstaxState(stacks = listOf(existingStack))
        val newStackValidator = NewStackValidatorImpl(ranstaxState)
        val newStack = Stack(paddedName, 1)

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
        val stack0 = Stack(name, 1)
        val ranstaxState = RanstaxState(
            stacks = listOf(stack0),
            stacksBeingEdited = listOf(stack0),
        )
        val existingNameValidator = NewStackValidatorImpl(ranstaxState)
        val stack1 = Stack(paddedName, 1)

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