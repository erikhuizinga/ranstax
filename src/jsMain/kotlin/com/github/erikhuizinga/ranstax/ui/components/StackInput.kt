package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticEvent
import com.github.erikhuizinga.ranstax.data.Stack
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.TextInput
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.KeyboardEvent

@Composable
fun StackInput(
    stack: Stack,
    onInput: (stack: Stack) -> Unit,
    onSubmit: () -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    val submitListener: (SyntheticEvent<EventTarget>) -> Unit = { event ->
        (event.nativeEvent as? KeyboardEvent)?.takeIf { it.key == "Enter" }?.let {
            onSubmit()
            it.preventDefault()
        }
    }
    Row {
        TextInput(value = stack.name) {
            placeholder("enter a name")
            onInput { onInput(stack.copy(name = it.value)) }
            onKeyUp(submitListener)
            onFocus { onEditingChange(true) }
            onBlur { onEditingChange(false) }
        }
        val minSize = 0
        val maxSize = Int.MAX_VALUE
        NumberInput(value = stack.size, min = minSize, max = maxSize) {
            placeholder("size")
            size(maxSize.toString().length)
            onInput {
                it.value?.toInt()?.takeIf { newSize -> newSize in minSize..maxSize }
                    ?.let { newSize -> onInput(stack.copy(size = newSize)) }
            }
            onKeyUp(submitListener)
            onFocus { onEditingChange(true) }
            onBlur { onEditingChange(false) }
        }
    }
}
