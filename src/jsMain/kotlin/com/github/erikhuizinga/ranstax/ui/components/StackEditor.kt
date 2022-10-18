package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.Stack
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackEditor(
    currentStack: Stack,
    isValidName: String.() -> Boolean,
    onSave: (savedStack: Stack) -> Unit,
    onDelete: () -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    var stack by remember { mutableStateOf(currentStack) }
    fun Stack.validate() = isValid && name.isValidName()
    Row {
        item {
            Button({
                if (stack.validate()) onClick {
                    onSave(stack.trimName())
                } else {
                    disabled()
                }
            }) {
                Text("💾")
            }
        }
        item {
            Button({ onClick { onDelete() } }) {
                Text("🗑")
            }
        }
        item {
            StackInput(
                stack = stack,
                onInput = { stack = it },
                onSubmit = { stack.takeIf { it.validate() }?.run(Stack::trimName)?.let(onSave) },
                onEditingChange = onEditingChange
            )
        }
    }
}
