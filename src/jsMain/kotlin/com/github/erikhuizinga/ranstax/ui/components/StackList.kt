package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.debug.DEBUG
import com.github.erikhuizinga.ranstax.domain.ExistingStackValidator
import com.github.erikhuizinga.ranstax.domain.NewStackValidatorImpl
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
                        onSave = { savedStack ->
                            onNewRanstaxStateTransform {
                                replace(id = idToRender, stack = savedStack, isBeingEdited = false)
                            }
                        },
                        onDelete = {
                            onNewRanstaxStateTransform {
                                copy(stateStacks = stateStacks.filterNot { it.id == idToRender })
                            }
                        },
                        onEditingChange = onEditingChange,
                        stackValidator = ExistingStackValidator(
                            stackToRender,
                            NewStackValidatorImpl(ranstaxState),
                        )
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
