package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.data.Stack
import com.github.erikhuizinga.ranstax.debug.log
import com.github.erikhuizinga.ranstax.domain.NewStackValidatorImpl
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
    ranstaxState: RanstaxState,
    stackValidator: Validator<Stack, StackValidation> = NewStackValidatorImpl(ranstaxState),
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
            val stackValidation = stackValidator(stack)
            log("NewStackInput: stackValidation = $stackValidation")

            val hintOrNull by remember(ranstaxState.isEditing, stackValidation) {
                derivedStateOf {
                    log("Evaluating hint or null")
                    stackValidation
                        .takeIf { it != StackValidation.NameBlank || ranstaxState.isEditing }
                        ?.hint
                }
            }
            log("NewStackInput: ranstaxState.isEditing = ${ranstaxState.isEditing}")
            log("NewStackInput: hintOrNull = $hintOrNull")

            fun onNewStackAndResetState() {
                onNewStack(stack.trimName())
                name = defaultName
                size = defaultSize
            }

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
                onEditingChange = onEditingChange,
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
