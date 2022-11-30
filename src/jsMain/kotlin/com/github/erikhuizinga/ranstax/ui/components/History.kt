package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun History(ranstaxState: RanstaxState, onReverseHistory: () -> Unit) {
    Column {
        val drawnStackNames = ranstaxState.drawnStackNames
        H3 {
            Text("📜 History")
        }
        if (drawnStackNames.isEmpty()) {
            SmallTextSpan("Draw to start history")
            return@Column
        }
        Div({
            classes(
                RanstaxStyle.history,
                RanstaxStyle.borderRadius,
                RanstaxStyle.visibleBorder,
            )
        }) {
            DisposableEffect(drawnStackNames.size, ranstaxState.isMostRecentHistoryOnTop) {
                fun scrollToMostRecentEdge() {
                    scopeElement.apply {
                        scrollTop = when (ranstaxState.isMostRecentHistoryOnTop) {
                            true -> 0.0
                            false -> scrollHeight.toDouble()
                        }
                    }
                }
                window.onresize = { scrollToMostRecentEdge() }
                scrollToMostRecentEdge()
                onDispose {}
            }

            val indexTemplate = "\$index"
            val nameTemplate = "\$name"
            val indexedNameTemplate = "$indexTemplate: $nameTemplate"
            val indexLength = ceil(
                log10(
                    (ranstaxState.totalStackSize + drawnStackNames.sumOf { it.size } + 1).toDouble()
                )
            ).roundToInt()
            var index = 0
            val historyEntries = drawnStackNames.map { drawActionStackNames ->
                "Drew ${drawActionStackNames.size} 👇" to
                        drawActionStackNames.map { stackName ->
                            val indexString = (++index).toString()
                            val padding =
                                "0".repeat((indexLength - indexString.length).coerceAtLeast(0))
                            indexedNameTemplate
                                .replace(indexTemplate, padding + indexString)
                                .replace(nameTemplate, stackName)
                        }
            }
            if (ranstaxState.isMostRecentHistoryOnTop) {
                historyEntries.reversed()
            } else {
                historyEntries
            }.forEachIndexed { drawActionIndex, (header, body) ->
                val actualIndex = if (ranstaxState.isMostRecentHistoryOnTop) {
                    historyEntries.lastIndex - drawActionIndex
                } else {
                    drawActionIndex
                }
                Div({
                    classes(
                        RanstaxStyle.historyEntry,
                        if (actualIndex % 2 == 0) {
                            RanstaxStyle.darkBackground
                        } else {
                            RanstaxStyle.lightBackground
                        }
                    )
                }) {
                    Text(header)
                    body.forEach {
                        Br()
                        Text(it)
                    }
                }
            }
        }
        val position = if (ranstaxState.isMostRecentHistoryOnTop) "top" else "bottom"
        Text("Showing most recent history at the $position")
        Button({ onClick { onReverseHistory() } }) {
            Text("🔃 reverse history")
        }
    }
}
