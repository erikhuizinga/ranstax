package com.github.erikhuizinga.ranstax.domain

interface Validator<in I, out O> {
    operator fun invoke(obj: I): O
}
