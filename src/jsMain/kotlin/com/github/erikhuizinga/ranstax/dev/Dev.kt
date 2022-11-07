package com.github.erikhuizinga.ranstax.dev

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.debug.log
import kotlinx.browser.document
import org.w3c.dom.url.URLSearchParams

val DEV = URLSearchParams(document.location?.search).get("dev").toBoolean()

fun setupDevRanstaxState(ranstaxState: RanstaxState) =
    if (ranstaxState.isEmpty) {
        log("Setting up dev RanstaxState")
        listOf(
            Stack(name = "💙 North America", size = 180),
            Stack(name = "💜 Europe", size = 81),
            Stack(name = "💛 Oceania", size = 95),
            Stack(name = "❤️ Asia", size = 90),
        ).fold(RanstaxState()) { state, stack -> state + stack }
            .also { log(it) }
    } else {
        ranstaxState
    }
