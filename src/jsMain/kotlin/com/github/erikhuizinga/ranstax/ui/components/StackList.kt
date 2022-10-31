package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
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
    if (ranstaxState.allStacks.isEmpty()) {
        return
    }
    Column {
        H3 {
            Text("📚 Stacks")
        }
        val stacksBeingEdited = ranstaxState.stacksBeingEdited
        Column({ classes(RanstaxStyle.tightColumn) }) {
            ranstaxState.allStacks.forEach { (stackToRender, isBeingEdited) ->
                if (isBeingEdited) {
                    StackEditor(
                        currentStack = stackToRender,
                        onSave = { savedStack ->
                            onNewRanstaxStateTransform {
                                copy(
                                    allStacks = allStacks
                                        .mapValues { if (it.key == stackToRender) false else it.value }
                                        .mapKeys { if (it.key == stackToRender) savedStack else it.key },
                                )
                            }
                        },
                        onDelete = {
                            onNewRanstaxStateTransform { copy(allStacks = allStacks - stackToRender) }
                        },
                        onEditingChange = onEditingChange,
                        stackValidator = ExistingStackValidator(
                            stackToRender,
                            NewStackValidatorImpl(ranstaxState),
                        )
                    )
                } else {
                    EditableStack(stackToRender) {
                        onNewRanstaxStateTransform {
                            copy(
                                allStacks = allStacks.mapValues { entry ->
                                    if (entry.key == stackToRender) true else entry.value
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
