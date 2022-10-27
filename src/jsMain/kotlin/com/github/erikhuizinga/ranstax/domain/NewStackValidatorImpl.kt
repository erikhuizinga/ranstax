package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.domain.StackValidation.NameBlank
import com.github.erikhuizinga.ranstax.domain.StackValidation.NameExists
import com.github.erikhuizinga.ranstax.domain.StackValidation.SizeMustBeAtLeastOne
import com.github.erikhuizinga.ranstax.domain.StackValidation.Valid

class NewStackValidatorImpl(
    private val ranstaxState: RanstaxState,
) : NewStackValidator {
    override operator fun invoke(obj: Stack) = when {
        obj.name.isBlank() -> NameBlank
        obj.size <= 0 -> SizeMustBeAtLeastOne
        obj.isExistingName -> NameExists
        else -> Valid
    }

    private val Stack.isExistingName: Boolean
        get() {
            val trimmedName = name.trim()

            val stacksBeingEdited = ranstaxState.stacksBeingEdited
            if (stacksBeingEdited.isNotEmpty() &&
                stacksBeingEdited.any { trimmedName == it.name }
            ) {
                return false
            }

            val stacks = ranstaxState.stacks
            return stacks.isNotEmpty() && stacks.any { trimmedName == it.name }
        }
}
