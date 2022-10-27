package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.Stack
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text

@Composable
fun EditableStack(
    stack: Stack,
    onEdit: () -> Unit,
) {
    Row {
        Button({ onClick { onEdit() } }) {
            Text("✏️")
        }
        StackComponent(stack)
    }
}
