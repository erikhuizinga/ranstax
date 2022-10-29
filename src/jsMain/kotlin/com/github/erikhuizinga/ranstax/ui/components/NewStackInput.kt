package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.domain.StackValidation
import com.github.erikhuizinga.ranstax.domain.Validator
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun NewStackInput(
    onNewStack: (newStack: Stack) -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
    stackValidator: Validator<Stack, StackValidation>,
) {
    Column {
        H3 {
            Text("🆕 New stack")
        }
        Row {
            val defaultName = ""
            val defaultSize = 10
            var name by remember { mutableStateOf(defaultName) }
            var size by remember { mutableStateOf(defaultSize) }
            val stack = Stack(name, size)

            fun onNewStackAndResetState() {
                onNewStack(stack.trimName())
                name = defaultName
                size = defaultSize
            }

            val stackValidation = stackValidator(stack)
            var isEditing by remember { mutableStateOf(false) }
            val hintOrNull = stackValidation
                .takeIf { it != StackValidation.NameBlank || isEditing }
                ?.hint

            StackInput(
                stack = stack,
                onInput = { (newName, newSize) ->
                    name = newName
                    size = newSize
                },
                onSubmit = {
                    if (stackValidation == StackValidation.Valid) {
                        onNewStackAndResetState()
                    }
                },
                onEditingChange = { newIsEditing ->
                    isEditing = newIsEditing
                    onEditingChange(newIsEditing)
                },
            )
            Button({
                if (stackValidation == StackValidation.Valid) onClick {
                    onNewStackAndResetState()
                } else {
                    disabled()
                }
            }) {
                Text("➕")
            }

            hintOrNull?.let { hint ->
                Text(hint)
            }
        }
    }
}
