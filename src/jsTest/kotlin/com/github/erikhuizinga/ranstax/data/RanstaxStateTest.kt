package com.github.erikhuizinga.ranstax.data

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RanstaxStateTest {
    @Test
    fun whenAStackIsAdded_ThenItIsInTheState() {
        val expected = Stack("", 0)

        val stateAfterAddition = RanstaxState() + expected

        assertTrue(expected in stateAfterAddition.stacks)
    }

    @Test
    fun whenAStackIsSubtracted_ThenItIsNotInTheState() {
        val unexpected = Stack("", 0)

        val stateAfterSubtraction =
            RanstaxState(stateStacks = listOf(StateStack(0, unexpected))) - unexpected

        assertFalse(unexpected in stateAfterSubtraction.stacks)
    }

    @Test
    fun whenAStackIsReplaced_ThenItIsRemovedAndTheNewOneIsInTheState() {
        val stackToReplace = Stack("unexpected", 0)
        val expected = stackToReplace.copy(name = "expected")
        val id = 0
        val ranstaxState = RanstaxState(stateStacks = listOf(StateStack(id, stackToReplace)))

        val replacedState = ranstaxState.replace(id, expected)

        assertFalse(stackToReplace in replacedState.stacks)
        assertTrue(expected in replacedState.stacks)
    }

    @Test
    fun whenIsEditingIsSetToTrueForAnId_ThenTheCorrespondingStackIsBeingEdited() {
        val stack = Stack("", 0)
        val id = 0
        val ranstaxState = RanstaxState(stateStacks = listOf(StateStack(id, stack, false)))

        val replacedState = ranstaxState.replace(id, true)

        assertTrue(stack in replacedState.stacksBeingEdited)
    }

    @Test
    fun whenIsEditingIsSetToFalseForAnId_ThenTheCorrespondingStackIsBeingEdited() {
        val stack = Stack("", 0)
        val id = 0
        val ranstaxState = RanstaxState(stateStacks = listOf(StateStack(id, stack, true)))

        val replacedState = ranstaxState.replace(id, false)

        assertFalse(stack in replacedState.stacksBeingEdited)
    }
}