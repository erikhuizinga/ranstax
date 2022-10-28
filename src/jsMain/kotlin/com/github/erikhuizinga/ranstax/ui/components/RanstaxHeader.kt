package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLDivElement

@Composable
fun RanstaxHeader(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
) {
    Div({ attrs?.invoke(this) }) {
        Text("Ranstax")
    }
}
