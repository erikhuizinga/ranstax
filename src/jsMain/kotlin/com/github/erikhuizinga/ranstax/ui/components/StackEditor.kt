package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.debug.DEBUG
import com.github.erikhuizinga.ranstax.domain.StackValidation
import com.github.erikhuizinga.ranstax.domain.Validator
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackEditor(
    currentStack: Stack,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
    stackValidator: Validator<Stack, StackValidation>,
    onEdit: (Stack) -> Unit,
) {
    Row {
        val stackValidation = stackValidator(currentStack)

        Button({
            if (stackValidation == StackValidation.Valid) onClick {
                onSave()
            } else {
                disabled()
            }
        }) {
            Text("💾")
        }
        Button({ onClick { onDelete() } }) {
            Text("🗑")
        }
        StackInput(
            stack = currentStack,
            onInput = { onEdit(it) },
            onSubmit = {
                if (stackValidation == StackValidation.Valid) {
                    onSave()
                }
            },
            onEditingChange = onEditingChange,
        )

        if (DEBUG) {
            Text(stackValidation.name)
        }

        stackValidation.hint?.also { hint ->
            Text(hint)
        }
    }
}
