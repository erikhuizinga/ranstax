package com.github.erikhuizinga.ranstax.domain

import com.github.erikhuizinga.ranstax.data.Stack

class StackValidator(private val existingStacks: Set<Stack>) : Validator<Stack, StackValidation> {
    override fun invoke(obj: Stack) = when {
        obj.name in existingStacks.map { it.name } -> StackValidation.NameExists
        obj.name.isBlank() -> StackValidation.NameBlank
        obj.size == 0 -> StackValidation.SizeMustBeAtLeastOne
        else -> StackValidation.Valid
    }
}
