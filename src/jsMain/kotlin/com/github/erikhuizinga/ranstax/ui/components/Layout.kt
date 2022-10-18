package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.Div

@Composable
fun Layout(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.layout) }) {
        content()
    }
}
