package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

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

            var index = 0
            val historyEntries = drawnStackNames.map { drawActionStackNames ->
                "${++index}: drew ${drawActionStackNames.size} 👇" to
                        drawActionStackNames
                            .groupingBy { it }
                            .eachCount()
                            .entries
                            .sortedBy { (drawnStackName) ->
                                ranstaxState.stacks.map { it.name }.indexOf(drawnStackName)
                            }
                            .map { (stackName, count) -> "$count from $stackName" }
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
                    Ul {
                        body.forEach {
                            Li {
                                Text(it)
                            }
                        }
                    }
                }
            }
        }
        val position = if (ranstaxState.isMostRecentHistoryOnTop) "top" else "bottom"
        Text("Showing most recent history at the $position")
        Button({ onClick { onReverseHistory() } }) {
            Text("🔃 Reverse history")
        }
    }
}
