package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun DrawButtons(
    ranstaxState: RanstaxState,
    onDraw: (number: Int) -> Unit,
) {
    H3 {
        Text("🫳 Draw")
    }
    Div({ classes(RanstaxStyle.numberButtonGrid) }) {
        (1..9).forEach { number ->
            Button({
                val numberButtonClass =
                    if (number % 2 == 0) RanstaxStyle.numberButton0 else RanstaxStyle.numberButton1
                classes(RanstaxStyle.largeButton, numberButtonClass)

                if (ranstaxState.isDrawButtonEnabled) onClick {
                    onDraw(number)
                } else {
                    disabled()
                }
            }) {
                Text("$number")
            }
        }
    }
}
