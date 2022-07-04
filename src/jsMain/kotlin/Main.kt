import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.web.events.SyntheticEvent
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.KeyboardEvent

typealias ComposableLambda = @Composable () -> Unit

fun main() {
    renderComposable(rootElementId = "ranstax") {
        val stacks = remember { mutableStateListOf<Stack>() }
        val stacksBeingEdited = remember { mutableStateListOf<Stack>() }

        // Debug stacks
        val northAmericaStack = Stack("North America", 180)
        stacks += northAmericaStack
        stacksBeingEdited += northAmericaStack
        stacks += Stack("Europe", 81)
        stacks += Stack("Oceania", 95)

        Div({ style { padding(24.px) } }) {
            H3 { Text("Stacks") }
            Div({ style { paddingBottom(8.px) } }) {
                NewStackInput(
                    isValidName = { stacks.none { it.name == trim() } },
                    onNewStack = { stacks += it },
                )
            }
            Div({ style { paddingVertical(8.px) } }) {
                StackList(
                    stacks, stacksBeingEdited
                )
            }
        }
    }
}

@Composable
private fun StackList(
    stacks: SnapshotStateList<Stack>,
    stacksBeingEdited: SnapshotStateList<Stack>,
) {
    stacks.map<Stack, ComposableLambda> { stack ->
        {
            Div({ style { paddingVertical(4.px) } }) {
                if (stack in stacksBeingEdited) {
                    StackEditor(
                        currentStack = stack,
                        isValidName = { stacksBeingEdited.any { it.name == this } || stacks.none { it.name == this } },
                        onSave = { savedStack ->
                            stacksBeingEdited -= stack
                            stacks[stacks.indexOf(stack)] = savedStack
                        },
                        onDelete = {
                            stacksBeingEdited -= stack
                            stacks -= stack
                        },
                    )
                } else {
                    Stack(stack) { stacksBeingEdited += stack }
                }
            }
        }
    }.ifEmpty { listOf<ComposableLambda> { Text("no stacks, add one here 👆") } }.forEach { Div { it() } }
}

@Composable
private fun NewStackInput(
    isValidName: String.() -> Boolean,
    onNewStack: (newStack: Stack) -> Unit,
) {
    val defaultName = ""
    val defaultSize = 10
    var name by remember { mutableStateOf(defaultName) }
    var size by remember { mutableStateOf(defaultSize) }

    Text("New stack 👉")
    Span({ style { paddingLeft(8.px) } }) {
        TextInput(value = name) {
            placeholder("enter a name")
            onInput { name = it.value }
        }
    }
    Span({ style { paddingHorizontal(8.px) } }) {
        val minSize = 0
        val maxSize = Int.MAX_VALUE
        NumberInput(value = size, min = minSize, max = maxSize) {
            placeholder("size")
            size(maxSize.toString().length)
            onInput {
                it.value?.toInt()?.takeIf { newSize -> newSize in minSize..maxSize }?.let { newSize -> size = newSize }
            }
        }
    }
    Button(attrs = {
        val newStack = Stack(name, size)
        if (newStack.isValid && newStack.name.isValidName()) onClick {
            name = defaultName
            size = defaultSize
            onNewStack(newStack)
        } else {
            disabled()
        }
    }) {
        Text("➕")
    }
}

@Composable
private fun StackInput(
    stack: Stack,
    onInput: (stack: Stack) -> Unit,
    onSubmit: () -> Unit,
) {
    val submitListener: (SyntheticEvent<EventTarget>) -> Unit = { event ->
        (event.nativeEvent as? KeyboardEvent)?.takeIf { it.key == "Enter" }?.let {
            onSubmit()
            it.preventDefault()
        }
    }
    TextInput(value = stack.name) {
        placeholder("enter a name")
        onInput { onInput(stack.copy(name = it.value)) }
        onKeyUp(submitListener)
    }
    Span({ style { paddingLeft(4.px) } }) {
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
        }
    }
}

@Composable
private fun Stack(
    stack: Stack,
    onEdit: () -> Unit,
) {
    Button({ onClick { onEdit() } }) {
        Text("✏️")
    }
    Span({ style { paddingLeft(8.px) } }) {
        Text("${stack.name}: ${stack.size}")
    }
}

@Composable
private fun StackEditor(
    currentStack: Stack,
    isValidName: String.() -> Boolean,
    onSave: (savedStack: Stack) -> Unit,
    onDelete: () -> Unit,
) {
    var stack by remember { mutableStateOf(currentStack) }

    fun Stack.validate() = isValid && name.isValidName()
    fun Stack.trimmedName() = stack.copy(name = this.name.trim())

    Button({
        if (stack.validate()) onClick {
            onSave(stack.trimmedName())
        } else {
            disabled()
        }
    }) {
        Text("💾")
    }
    Span({ style { paddingHorizontal(8.px) } }) {
        StackInput(
            stack = stack,
            onInput = { stack = it },
            onSubmit = { stack.takeIf { it.validate() }?.run(Stack::trimmedName)?.let(onSave) },
        )
    }
    Button({ onClick { onDelete() } }) {
        Text("🗑")
    }
}

fun StyleScope.paddingVertical(value: CSSNumeric) {
    paddingTop(value)
    paddingBottom(value)
}

fun StyleScope.paddingHorizontal(value: CSSNumeric) {
    paddingLeft(value)
    paddingRight(value)
}
