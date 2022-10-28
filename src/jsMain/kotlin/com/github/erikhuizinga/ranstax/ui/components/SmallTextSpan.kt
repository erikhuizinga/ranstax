package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun SmallTextSpan(value: String) {
    Span({ classes(RanstaxStyle.smallFont) }) {
        Text(value)
    }
}
