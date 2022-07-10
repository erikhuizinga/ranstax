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
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.whiteSpace
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
        Style(RanstaxStyle)
        Layout(
            { RanstaxHeader() },
            { RanstaxApp() },
        )
    }
}

object RanstaxStyle : StyleSheet() {
    init {
        universal style {
            boxSizing("border-box")
            margin(0.px)
        }
        "html, body" style {
            height(100.percent)
        }
    }

    val layout by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
        margin(0.px)
        height(100.percent)
    }
}

@Composable
fun Layout(vararg composables: @Composable () -> Unit) {
    Div({ style { classes(RanstaxStyle.layout) } }) {
        composables.forEach { it() }
    }
}

@Composable
fun RanstaxHeader() {
    Div({
        style {
            fontFamily("monospace")
            whiteSpace("break-spaces")
            margin(16.px)
        }
    }) {
        ranstaxHeaders.random().lines().forEach {
            Div {
                Text(it)
            }
        }
    }
}

@Composable
private fun RanstaxApp() {
    val stacks: SnapshotStateList<Stack> = remember { mutableStateListOf() }
    val stacksBeingEdited: SnapshotStateList<Stack> = remember { mutableStateListOf() }
    var lastDrawnStack: Stack? by remember { mutableStateOf(null) }

    // Debug stacks
    val northAmericaStack = Stack("North America", 180)
    stacks += northAmericaStack
    // stacksBeingEdited += northAmericaStack
    stacks += Stack("Europe", 81)
    stacks += Stack("Oceania", 95)

    Div {
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
            H2 {
                Text("DRAW")
            }
        }
        lastDrawnStack?.let {
            Span {
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
        Div {
            NewStackInput(
                isValidName = { stacks.none { it.name == trim() } },
                onNewStack = { stacks += it },
            )
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
        Div {
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
    Span {
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
    Span {
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
    Span {
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
    Span {
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
    Span {
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
