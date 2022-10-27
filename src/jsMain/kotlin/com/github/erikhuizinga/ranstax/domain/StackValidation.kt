package com.github.erikhuizinga.ranstax.domain;

enum class StackValidation : HasHint {
    Valid {
        override val hint = null
    },
    SizeMustBeAtLeastOne {
        override val hint = "⚠️ Size must be greater than 0"
    },
    NameBlank {
        override val hint = "⚠️ Name must not be blank"
    },
    NameExists {
        override val hint = "⚠️ Name must not exist already"
    };
}
