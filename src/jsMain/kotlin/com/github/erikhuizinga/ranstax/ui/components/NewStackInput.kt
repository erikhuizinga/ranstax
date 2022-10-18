package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.Stack
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

@Composable
fun NewStackInput(
    isValidName: String.() -> Boolean,
    onNewStack: (newStack: Stack) -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    val defaultName = ""
    val defaultSize = 10
    var name by remember { mutableStateOf(defaultName) }
    var size by remember { mutableStateOf(defaultSize) }

    fun Stack.validate() = isValid && name.isValidName()

    val stack = Stack(name, size)

    fun onNewStackAndResetState() {
        onNewStack(stack.trimName())
        name = defaultName
        size = defaultSize
    }

    H3 {
        Text("🆕 New stack")
    }
    Row {
        item {
            StackInput(
                stack = stack,
                onInput = { (newName, newSize) ->
                    name = newName
                    size = newSize
                },
                onSubmit = {
                    if (stack.validate()) {
                        onNewStackAndResetState()
                    }
                },
                onEditingChange = onEditingChange,
            )
        }
        item {
            Button({
                if (stack.validate()) onClick {
                    onNewStackAndResetState()
                } else {
                    disabled()
                }
            }) {
                Text("➕")
            }
        }
    }
}
