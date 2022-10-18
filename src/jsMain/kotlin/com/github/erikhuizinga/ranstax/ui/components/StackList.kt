package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
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
    H3 {
        Text("📚 Stacks")
    }
    val stacksBeingEdited = ranstaxState.stacksBeingEdited
    Column {
        stacks.forEach { stack ->
            item {
                if (stack in stacksBeingEdited) {
                    StackEditor(
                        currentStack = stack,
                        isValidName = {
                            val trimmedName = trim()
                            stacksBeingEdited.any { it.name == trimmedName } || stacks.none { it.name == trimmedName }
                        },
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
