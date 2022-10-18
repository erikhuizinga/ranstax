package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun Clear(onReset: () -> Unit) {
    H3 {
        Text("🔁 Clear everything")
    }
    Button({
        classes(RanstaxStyle.mediumButton)
        onClick {
            if (window.confirm(
                    "Do you really want to clear all data?" +
                            " If you choose to clear, you will lose all current data."
                )
            ) {
                onReset()
            }
        }
    }) {
        Text("Clear")
    }
}
