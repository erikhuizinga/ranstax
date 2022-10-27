package com.github.erikhuizinga.ranstax.dev

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import kotlinx.browser.document
import org.w3c.dom.url.URLSearchParams

val DEV = URLSearchParams(document.location?.search).get("dev").toBoolean()

fun setupDevRanstaxState(ranstaxState: RanstaxState) =
    if (ranstaxState.totalStackSize == 0) {
        val northAmericaStack = Stack("💙 North America", 180)
        val europeStack = Stack("💜 Europe", 81)
        val oceaniaStack = Stack("💛 Oceania", 95)
        val asiaStack = Stack("❤️ Asia", 90)
        val stacks = listOf(northAmericaStack, europeStack, oceaniaStack, asiaStack)
        val stacksBeingEdited: List<Stack> = listOf(/* northAmericaStack */)
        RanstaxState(stacks, stacksBeingEdited)
    } else {
        ranstaxState
    }
