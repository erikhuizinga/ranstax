package com.github.erikhuizinga.ranstax.dev

import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.debug.log
import kotlinx.browser.document
import org.w3c.dom.url.URLSearchParams

val DEV = URLSearchParams(document.location?.search).get("dev").toBoolean()

fun setupDevRanstaxState(ranstaxState: RanstaxState) =
    if (ranstaxState.totalStackSize == 0) {
        log("Setting up dev stacks")
        RanstaxState(
            listOf(
                Stack("💙 North America", 180),
                Stack("💜 Europe", 81),
                Stack("💛 Oceania", 95),
                Stack("❤️ Asia", 90),
            ).associateWith { false }
        )
    } else {
        ranstaxState
    }
