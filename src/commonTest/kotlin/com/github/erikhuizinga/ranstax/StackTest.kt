package com.github.erikhuizinga.ranstax

import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class StackTest {
    @Test
    fun stackConstructionWith0SizeThrowsIAE() {
        assertFailsWith<IllegalArgumentException> { Stack(0) }
    }
}
