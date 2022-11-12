package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.debug.DEBUG
import com.github.erikhuizinga.ranstax.domain.StackValidator
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackList(
    ranstaxState: RanstaxState,
    onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    if (!ranstaxState.hasStacks) {
        return
    }
    Column {
        H3 {
            Text("📚 Stacks")
        }
        Column({ classes(RanstaxStyle.tightColumn) }) {
            ranstaxState.stateStacks.forEach { stateStack ->
                if (DEBUG) {
                    Text(stateStack.toString())
                }
                val (idToRender, stackToRender, isBeingEdited) = stateStack
                if (isBeingEdited) {
                    StackEditor(
                        currentStack = stackToRender,
                        onSave = {
                            onNewRanstaxStateTransform {
                                replace(id = idToRender, isBeingEdited = false)
                            }
                        },
                        onDelete = {
                            onNewRanstaxStateTransform {
                                this - stackToRender
                            }
                        },
                        onEditingChange = onEditingChange,
                        stackValidator = StackValidator(ranstaxState.stacksNotBeingEdited.toSet()),
                        onEdit = { editedStack ->
                            onNewRanstaxStateTransform {
                                replace(idToRender, editedStack)
                            }
                        },
                    )
                } else {
                    EditableStackNameAndSize(stackToRender) {
                        onNewRanstaxStateTransform {
                            replace(id = idToRender, isBeingEdited = true)
                        }
                    }
                }
            }
        }
    }
}
