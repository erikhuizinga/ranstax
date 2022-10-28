package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun SmallText(value: String) {
    Div({ classes(RanstaxStyle.smallFont) }) {
        Text(value)
    }
}
