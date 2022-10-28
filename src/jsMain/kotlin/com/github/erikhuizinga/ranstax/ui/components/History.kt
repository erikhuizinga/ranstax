package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun History(ranstaxState: RanstaxState) {
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
            DisposableEffect(drawnStackNames.size) {
                fun scrollToEnd() {
                    scopeElement.apply { scrollTop = scrollHeight.toDouble() }
                }
                window.onresize = { scrollToEnd() }
                scrollToEnd()
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
            drawnStackNames.forEach { drawActionStackNames ->
                Div {
                    Text("Drew ${drawActionStackNames.size} 👇")
                }
                drawActionStackNames.map { stackName ->
                    val indexString = (++index).toString()
                    indexedNameTemplate.replace(
                        indexTemplate, "0".repeat(indexLength - indexString.length) + indexString
                    ).replace(nameTemplate, stackName)
                }.forEach {
                    Div {
                        Text(it)
                    }
                }
            }
        }
    }
}
