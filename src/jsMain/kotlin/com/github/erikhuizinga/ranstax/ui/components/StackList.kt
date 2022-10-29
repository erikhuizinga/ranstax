package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackList(
    ranstaxState: RanstaxState,
    onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit,
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
        Column({ classes(RanstaxStyle.tightColumn) }) {
            stacks.forEach { stack ->
                if (stack in stacksBeingEdited) {
                    StackEditor(
                        currentStack = stack,
                        onSave = { savedStack ->
                            onNewRanstaxStateTransform {
                                copy(
                                    stacks = stacks.map { if (it == stack) savedStack else it },
                                    stacksBeingEdited = stacksBeingEdited - stack,
                                )
                            }
                        },
                        onDelete = {
                            onNewRanstaxStateTransform {
                                copy(
                                    stacks = stacks - stack,
                                    stacksBeingEdited = stacksBeingEdited - stack,
                                )
                            }
                        },
                        onEditingChange = onEditingChange,
                        ranstaxState = ranstaxState,
                    )
                } else {
                    EditableStack(stack) {
                        onNewRanstaxStateTransform {
                            copy(stacksBeingEdited = stacksBeingEdited + stack)
                        }
                    }
                }
            }
        }
    }
}
