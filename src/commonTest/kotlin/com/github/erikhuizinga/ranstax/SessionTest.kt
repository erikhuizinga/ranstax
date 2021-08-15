package com.github.erikhuizinga.ranstax

import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class SessionTest {
    @Test
    fun sessionConstructorWithoutStacksThrowsIAE() {
        assertFailsWith<IllegalArgumentException> { Session() }
        assertFailsWith<IllegalArgumentException> { Session(emptyList()) }
    }
}
