package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.css.rowGap
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackList(
    ranstaxState: RanstaxState,
    onNewRanstaxState: (RanstaxState) -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    val stacks = ranstaxState.stacks
    if (stacks.isEmpty()) {
        return
    }
    Column {
        H3 {
            Text("📚 Stacks")
        }
        val stacksBeingEdited = ranstaxState.stacksBeingEdited
        Column({ style { rowGap(RanstaxStyle.smallSize) } }) {
            stacks.forEach { stack ->
                if (stack in stacksBeingEdited) {
                    StackEditor(
                        currentStack = stack,
                        onSave = { savedStack ->
                            onNewRanstaxState(
                                ranstaxState.copy(
                                    stacks = stacks.map { if (it == stack) savedStack else it },
                                    stacksBeingEdited = stacksBeingEdited - stack,
                                )
                            )
                        },
                        onDelete = {
                            onNewRanstaxState(
                                ranstaxState.copy(
                                    stacks = stacks - stack,
                                    stacksBeingEdited = stacksBeingEdited - stack,
                                )
                            )
                        },
                        onEditingChange = onEditingChange,
                        ranstaxState = ranstaxState,
                    )
                } else {
                    EditableStack(stack) {
                        onNewRanstaxState(
                            ranstaxState.copy(stacksBeingEdited = stacksBeingEdited + stack)
                        )
                    }
                }
            }
        }
    }
}
