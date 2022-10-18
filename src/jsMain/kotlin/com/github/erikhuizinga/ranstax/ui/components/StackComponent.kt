package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.Stack
import org.jetbrains.compose.web.dom.Text

@Composable
fun StackComponent(stack: Stack) {
    Text("${stack.name}: ${stack.size}")
}
