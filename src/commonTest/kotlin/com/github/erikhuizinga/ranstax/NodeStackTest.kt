package com.github.erikhuizinga.ranstax

import kotlin.test.Test
import kotlin.test.assertEquals

internal class NodeStackTest {
    @Test
    fun nameEqualsStackListName() {
        val stacks = List(5) { LeafStack(name = it.toString()) }
        val expected = stacks.map(Stack::name).toString()

        val actual = NodeStack(stacks).name

        assertEquals(expected, actual)
    }

    @Test
    fun sizeEqualsStackListSizeSum() {
        val stacks = List(5) { LeafStack(size = it + 1) }
        val expected = stacks.sumOf(Stack::size)

        val actual = NodeStack(stacks).size

        assertEquals(expected, actual)
    }
}
