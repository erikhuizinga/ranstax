package com.github.erikhuizinga.ranstax

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.debug.log
import com.github.erikhuizinga.ranstax.dev.DEV
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import com.github.erikhuizinga.ranstax.ui.components.Layout
import com.github.erikhuizinga.ranstax.ui.components.RanstaxApp
import com.github.erikhuizinga.ranstax.ui.components.RanstaxHeader
import kotlinx.browser.localStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.get
import org.w3c.dom.set

fun main() {
    log("Debugging enabled")

    if (DEV) {
        log("Setting up dev stacks")
        setupDevStacks()
    }

    renderComposable(rootElementId = "ranstax") {
        Style(RanstaxStyle)
        var ranstaxState by remember { mutableStateOf(loadRanstaxState()) }
        Layout {
            RanstaxHeader()
            RanstaxApp(ranstaxState) { newRanstaxState ->
                storeRanstaxState(newRanstaxState)
                ranstaxState = newRanstaxState
            }
        }
    }
}

private fun setupDevStacks() {
    if (loadRanstaxState().stacks.sumOf { it.size } == 0) {
        val northAmericaStack = Stack("North America", 180)
        val europeStack = Stack("Europe", 81)
        val oceaniaStack = Stack("Oceania", 95)
        val asiaStack = Stack("Asia", 90)
        val stacks = listOf(northAmericaStack, europeStack, oceaniaStack, asiaStack)
        val stacksBeingEdited: List<Stack> = listOf(/* northAmericaStack */)
        storeRanstaxState(RanstaxState(stacks, stacksBeingEdited))
    }
}

private const val RANSTAX_STATE_KEY = "RanstaxState"

private fun storeRanstaxState(ranstaxState: RanstaxState) {
    localStorage[RANSTAX_STATE_KEY] = Json.encodeToString(ranstaxState)
}

private fun loadRanstaxState(): RanstaxState =
    localStorage[RANSTAX_STATE_KEY]?.let(Json.Default::decodeFromString) ?: RanstaxState()
