package com.github.erikhuizinga.ranstax.dev

import com.github.erikhuizinga.ranstax.data.RanstaxState
import kotlin.test.Test
import kotlin.test.assertTrue

class SetupDevRanstaxState {
    @Test
    fun emptyStateSetupReturnsStacks() {
        val actual = setupDevRanstaxState(RanstaxState())

        assertTrue(actual.hasStacks)
    }
}