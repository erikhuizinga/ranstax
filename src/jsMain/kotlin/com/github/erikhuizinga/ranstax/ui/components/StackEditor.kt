package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.domain.ExistingStackValidator
import com.github.erikhuizinga.ranstax.domain.StackValidation
import com.github.erikhuizinga.ranstax.domain.StackValidation.Valid
import com.github.erikhuizinga.ranstax.domain.Validator
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackEditor(
    currentStack: Stack,
    onSave: (savedStack: Stack) -> Unit,
    onDelete: () -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
    ranstaxState: RanstaxState,
    stackValidator: Validator<Stack, StackValidation> = ExistingStackValidator(ranstaxState),
) {
    Row {
        var stack by remember { mutableStateOf(currentStack) }
        val stackValidation = stackValidator(stack)

        Button({
            if (stackValidation == Valid) onClick {
                onSave(stack.trimName())
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
            stack = stack,
            onInput = { stack = it },
            onSubmit = {
                if (stackValidation == Valid) {
                    onSave(stack.trimName())
                }
            },
            onEditingChange = onEditingChange,
        )
        stackValidation.hint?.also { hint ->
            Text(hint)
        }
    }
}
