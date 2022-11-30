package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun StateButtons(
    onSaveStacks: () -> Unit,
    canLoad: Boolean,
    onLoad: () -> Unit,
    onClearHistory: () -> Unit,
    canClear: Boolean,
    onClear: () -> Unit,
) {
    Column {
        H3 {
            Text("🗃️ Storage")
        }
        Button({
            classes(RanstaxStyle.mediumButton)
            onClick {
                if (!canLoad || window.confirm(
                        "Do you really want to save?" +
                                " Your current stacks will overwrite the previously saved stacks."
                    )
                ) {
                    onSaveStacks()
                }
            }
        }) {
            Text("⤵️ Save stacks")
        }
        Button({
            classes(RanstaxStyle.mediumButton)
            if (canLoad) onClick {
                if (window.confirm(
                        "Do you really want to load? If you choose to load," +
                                " your stacks will change to the stacks that were saved earlier."
                    )
                ) {
                    onLoad()
                }
            } else {
                disabled()
            }
        }) {
            Text("⤴️ Load saved stacks")
        }
        Button({
            classes(RanstaxStyle.mediumButton)
            onClick {
                if (window.confirm(
                        "Do you really want to clear the history?" +
                                " If you choose to do so, the history will become empty."
                    )
                ) {
                    onClearHistory()
                }
            }
        }) {
            Text("🧹 Clear history")
        }
        Button({
            classes(RanstaxStyle.mediumButton)
            if (canClear) onClick {
                if (window.confirm(
                        "Do you really want to clear all data?" +
                                " If you choose to clear, you will lose all current data."
                    )
                ) {
                    onClear()
                }
            } else {
                disabled()
            }
        }) {
            Text("✨ Clear everything")
        }
    }
}
