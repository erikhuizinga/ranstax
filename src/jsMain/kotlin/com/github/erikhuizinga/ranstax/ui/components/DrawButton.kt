package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

@Composable
fun DrawButton(
    ranstaxState: RanstaxState,
    onDraw: () -> Unit,
) {
    Button({
        classes(RanstaxStyle.largeButton)
        if (ranstaxState.isDrawButtonEnabled) onClick {
            onDraw()
        } else {
            disabled()
        }
    }) {
        Text("Draw")
    }
}
