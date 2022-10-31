package com.github.erikhuizinga.ranstax

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.debug.log
import com.github.erikhuizinga.ranstax.dev.DEV
import com.github.erikhuizinga.ranstax.dev.setupDevRanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import com.github.erikhuizinga.ranstax.ui.components.Footer
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

    val initialRanstaxState = if (DEV) {
        log("Setting up dev stacks")
        setupDevRanstaxState(loadRanstaxState())
    } else {
        loadRanstaxState()
    }

    renderComposable(rootElementId = "ranstax") {
        Style(RanstaxStyle)
        var ranstaxState by remember { mutableStateOf(initialRanstaxState) }
        val onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit = { transform ->
            val newRanstaxState = ranstaxState.transform()
            storeRanstaxState(newRanstaxState)
            ranstaxState = newRanstaxState
        }
        Layout {
            RanstaxHeader { classes(RanstaxStyle.header) }
            RanstaxApp(
                { classes(RanstaxStyle.mainContent) },
                ranstaxState,
                onNewRanstaxStateTransform,
            )
            Footer { classes(RanstaxStyle.footer) }
        }
    }
}

private const val RANSTAX_STATE_KEY = "RanstaxState"

private val json = Json { allowStructuredMapKeys = true }

private fun storeRanstaxState(ranstaxState: RanstaxState) {
    localStorage[RANSTAX_STATE_KEY] = json.encodeToString(ranstaxState)
}

private fun loadRanstaxState() = runCatching<RanstaxState?> {
    localStorage[RANSTAX_STATE_KEY]?.let { json.decodeFromString(it) }
}.onFailure { log(it) }.getOrNull() ?: RanstaxState()
