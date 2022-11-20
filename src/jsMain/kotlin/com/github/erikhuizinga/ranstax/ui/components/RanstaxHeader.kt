package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement

@Composable
fun RanstaxHeader(
    rowAttrs: AttrBuilderContext<HTMLDivElement>? = null,
    titleAttrs: AttrBuilderContext<HTMLDivElement>? = null,
) {
    Row({ rowAttrs?.invoke(this) }) {
        Div({ titleAttrs?.invoke(this) }) {
            Text("Ranstax")
        }
        A(href = "https://github.com/erikhuizinga/ranstax/discussions/1") {
            Text("ℹ️ How does Ranstax work?")
        }
    }
}
