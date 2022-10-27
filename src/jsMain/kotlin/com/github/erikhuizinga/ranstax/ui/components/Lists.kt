package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Composable
fun Row(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable () -> Unit,
) {
    Div({
        classes(RanstaxStyle.row)
        attrs?.invoke(this)
    }) {
        content()
    }
}

@Composable
fun Column(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable () -> Unit,
) {
    Div({
        classes(RanstaxStyle.column)
        attrs?.invoke(this)
    }) {
        content()
    }
}
