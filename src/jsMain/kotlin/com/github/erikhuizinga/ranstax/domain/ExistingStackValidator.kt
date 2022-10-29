package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack

class ExistingStackValidator(
    private val ranstaxState: RanstaxState,
    private val stackBeingEdited: Stack,
    private val newStackValidator: NewStackValidator = NewStackValidatorImpl(ranstaxState),
) : Validator<Stack, StackValidation> {
    override operator fun invoke(obj: Stack) = when (val stackValidation = newStackValidator(obj)) {
        StackValidation.NameExists -> if (obj.name == stackBeingEdited.name) {
            StackValidation.Valid
        } else {
            StackValidation.NameExists
        }

        else -> stackValidation
    }
}
