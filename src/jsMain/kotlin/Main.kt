import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.web.events.SyntheticEvent
import kotlin.math.max
import kotlin.random.Random
import org.jetbrains.compose.web.attributes.cols
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.rows
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
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.TextInput
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.KeyboardEvent

fun main() {
    renderComposable(rootElementId = "ranstax") {
        val stacks: SnapshotStateList<Stack> = remember { mutableStateListOf() }
        val stacksBeingEdited: SnapshotStateList<Stack> = remember { mutableStateListOf() }
        var lastDrawnStack: Stack? by remember { mutableStateOf(null) }

        // Debug stacks
        val northAmericaStack = Stack("North America", 180)
        stacks += northAmericaStack
        // stacksBeingEdited += northAmericaStack
        stacks += Stack("Europe", 81)
        stacks += Stack("Oceania", 95)

        Div({ style { padding(24.px) } }) {
            Button({
                style {
                    val totalSize = stacks.sumOf { it.size }
                    if (totalSize > 0 && stacksBeingEdited.isEmpty()) onClick {
                        var chosenIndex = Random.nextInt(totalSize)
                        val chosenStack = stacks.first {
                            chosenIndex -= it.size
                            chosenIndex < 0
                        }
                        lastDrawnStack = chosenStack
                        stacks[stacks.indexOf(chosenStack)] = chosenStack.copy(size = chosenStack.size - 1)
                    } else {
                        disabled()
                    }
                }
            }) {
                H2({ style { padding(8.px) } }) {
                    Text("DRAW")
                }
            }
            lastDrawnStack?.let {
                Span({ style { paddingLeft(8.px) } }) {
                    val firstLine = "Drawn from:"
                    val value = "$firstLine\n${it.name}"
                    TextArea(value) {
                        style { property("resize", "none") }
                        disabled()
                        val valueLines = value.split('\n')
                        rows(valueLines.count())
                        cols(max(valueLines.maxOf { it.length }, stacks.maxOf { (name) -> name.length }))
                        contentEditable(false)
                    }
                }
            }
            H3 {
                Text("Stacks")
            }
            Div {
                StackList(stacks, stacksBeingEdited)
            }
            Div({ style { paddingTop(8.px) } }) {
                NewStackInput(
                    isValidName = { stacks.none { it.name == trim() } },
                    onNewStack = { stacks += it },
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
    if (stacks.isEmpty()) {
        Text("No stacks, add one here 👇")
    }
    for (stack in stacks) {
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
                EditableStack(stack) { stacksBeingEdited += stack }
            }
        }
    }
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

    fun Stack.validate() = isValid && name.isValidName()

    val stack = Stack(name, size)

    fun onNewStackAndResetState() {
        onNewStack(stack)
        name = defaultName
        size = defaultSize
    }

    Text("New stack 👉")
    Span({ style { paddingLeft(8.px) } }) {
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
        )
    }
    Span({ style { paddingLeft(4.px) } }) {
        Button(attrs = {
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
private fun EditableStack(
    stack: Stack,
    onEdit: () -> Unit,
) {
    Button({ onClick { onEdit() } }) {
        Text("✏️")
    }
    Span({ style { paddingLeft(8.px) } }) {
        Stack(stack)
    }
}

@Composable
private fun Stack(stack: Stack) {
    Text("${stack.name}: ${stack.size}")
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
        Button({ onClick { onDelete() } }) {
            Text("🗑")
        }
    }
    StackInput(
        stack = stack,
        onInput = { stack = it },
        onSubmit = { stack.takeIf { it.validate() }?.run(Stack::trimmedName)?.let(onSave) },
    )
}

fun StyleScope.paddingVertical(value: CSSNumeric) {
    paddingTop(value)
    paddingBottom(value)
}

fun StyleScope.paddingHorizontal(value: CSSNumeric) {
    paddingLeft(value)
    paddingRight(value)
}
