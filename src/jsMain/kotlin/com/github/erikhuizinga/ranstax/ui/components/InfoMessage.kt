package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun InfoMessage(ranstaxState: RanstaxState) {
    Div({ classes(RanstaxStyle.smallFont) }) {
        Text(
            when (ranstaxState.hasStacks) {
                true -> when (ranstaxState.areAllStacksEmpty) {
                    true -> "🫥 Nothing left to draw, stacks are empty"
                    false -> when (ranstaxState.stacksBeingEdited.isEmpty()) {
                        true -> "ℹ️ Press any number key on your keyboard to draw that many items"
                        false -> "⚠️ Finish editing all stacks to enable drawing"
                    }
                }

                false -> "No stacks, add some new ones"
            }
        )
    }
}
