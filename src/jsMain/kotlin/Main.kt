import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.web.events.SyntheticEvent
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
import org.w3c.dom.get
import org.w3c.dom.set

private const val debug = true

private fun log(m: String) {
    if (debug) println(m)
}

fun main() {
    if (debug) setupDebugStacks()

    renderComposable(rootElementId = "ranstax") {
        Style(RanstaxStyle)
        var ranstaxState by remember { mutableStateOf(loadRanstaxState()) }
        Layout(
            { RanstaxHeader() },
            {
                RanstaxApp(ranstaxState) { newRanstaxState ->
                    storeRanstaxState(newRanstaxState)
                    ranstaxState = newRanstaxState
                }
            },
        )
    }
}

private fun setupDebugStacks() {
    if (loadRanstaxState().stacks.sumOf { it.size } == 0) {
        val northAmericaStack = Stack("North America", 5)
        val europeStack = Stack("Europe", 3)
        val oceaniaStack = Stack("Oceania", 4)
        val stacks = listOf(northAmericaStack, europeStack, oceaniaStack)
        val stacksBeingEdited: List<Stack> = listOf(/* northAmericaStack */)
        storeRanstaxState(RanstaxState(stacks, stacksBeingEdited))
    }
}

private const val RANSTAX_STATE_KEY = "RanstaxState"

private fun storeRanstaxState(ranstaxState: RanstaxState) {
    localStorage[RANSTAX_STATE_KEY] = Json.encodeToString(ranstaxState)
    log("Stored $ranstaxState")
}

private fun loadRanstaxState(): RanstaxState {
    val ranstaxState =
        localStorage[RANSTAX_STATE_KEY]?.let(Json.Default::decodeFromString) ?: RanstaxState()
    log("Read $ranstaxState")
    return ranstaxState
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
    val app by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Normal)
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
        Text(ranstaxHeaders.random())
    }
}

@Serializable
private data class RanstaxState(
    val stacks: List<Stack> = emptyList(),
    val stacksBeingEdited: List<Stack> = emptyList(),
    val lastDrawnStackNames: List<String> = emptyList(),
) {
    init {
        require(stacks.containsAll(stacksBeingEdited)) {
            "stacks (${stacks.joinToString()}) must contain all stacksBeingEdited (${stacksBeingEdited.joinToString()}). Stacks not in stacks: " + (stacksBeingEdited - stacks.toSet()).joinToString()
        }
    }
}

@Composable
private fun RanstaxApp(ranstaxState: RanstaxState, onNewRanstaxState: (RanstaxState) -> Unit) {
    val (stacks, stacksBeingEdited, lastDrawnStackNames) = ranstaxState
    Div(attrs = { style { classes(RanstaxStyle.app) } }) {
        Div {
            DrawButton(
                stacks,
                stacksBeingEdited,
                onNewRanstaxState,
                ranstaxState,
                lastDrawnStackNames,
            )
        }
        Div {
            History(stacks, lastDrawnStackNames)
        }
        H3 {
            Text("Stacks")
        }
        Div {
            StackList(ranstaxState, onNewRanstaxState)
        }
        Div {
            NewStackInput(
                isValidName = { stacks.none { it.name == trim() } },
                onNewStack = {
                    onNewRanstaxState(ranstaxState.copy(stacks = ranstaxState.stacks + it))
                },
            )
        }
        Div {
            ResetButton { onNewRanstaxState(RanstaxState()) }
        }
    }
}

@Composable
private fun DrawButton(
    stacks: List<Stack>,
    stacksBeingEdited: List<Stack>,
    onNewRanstaxState: (RanstaxState) -> Unit,
    ranstaxState: RanstaxState,
    lastDrawnStackNames: List<String>,
) {
    Button({
        style {
            val totalSize = stacks.sumOf { it.size }
            if (totalSize > 0 && stacksBeingEdited.isEmpty()) onClick {
                var chosenIndex = Random.nextInt(totalSize)
                val chosenStack = stacks.first {
                    chosenIndex -= it.size
                    chosenIndex < 0
                }
                onNewRanstaxState(
                    ranstaxState.copy(
                        stacks = stacks.map {
                            if (it == chosenStack) {
                                chosenStack.copy(size = chosenStack.size - 1)
                            } else {
                                it
                            }
                        },
                        lastDrawnStackNames = lastDrawnStackNames + chosenStack.name,
                    )
                )
            } else {
                disabled()
            }
        }
    }) {
        H2 {
            Text("DRAW")
        }
    }
}

@Composable
private fun History(
    stacks: List<Stack>,
    lastDrawnStackNames: List<String>,
) {
    Span {
        val indexLength =
            ceil(
                log10((stacks.sumOf { it.size } + lastDrawnStackNames.size + 1).toDouble())
            ).roundToInt()

        val indexTemplate = "\$index"
        val nameTemplate = "\$name"

        val indexedNameTemplate = "$indexTemplate: $nameTemplate"
        val indexedNameTemplateLength = indexedNameTemplate
            .replace(indexTemplate, "0".repeat(indexLength))
            .replace(
                nameTemplate,
                (stacks.map { it.name } + lastDrawnStackNames.toSet()).toSet()
                    .maxByOrNull { it.length } ?: ""
            )
            .length

        val indexedNames = lastDrawnStackNames.mapIndexed { index, lastDrawnStackName ->
            val indexString = (index + 1).toString()
            indexedNameTemplate
                .replace(
                    indexTemplate,
                    "0".repeat(indexLength - indexString.length) + indexString
                )
                .replace(nameTemplate, lastDrawnStackName)
        }.reversed().joinToString(separator = "\n")

        TextArea("Drew from:\n$indexedNames") {
            style {
                property("resize", "none")
                property("overflow", "scroll")
            }
            disabled()
            rows(8)
            cols(indexedNameTemplateLength)
            contentEditable(false)
        }
    }
}

@Composable
private fun StackList(
    ranstaxState: RanstaxState,
    onNewRanstaxState: (RanstaxState) -> Unit,
) {
    val stacks = ranstaxState.stacks
    if (stacks.isEmpty()) {
        Text("No stacks, add one here 👇")
    }
    for (stack in stacks) {
        Div {
            val stacksBeingEdited = ranstaxState.stacksBeingEdited
            if (stack in stacksBeingEdited) {
                StackEditor(
                    currentStack = stack,
                    isValidName = { stacksBeingEdited.any { it.name == this } || stacks.none { it.name == this } },
                    onSave = { savedStack ->
                        onNewRanstaxState(
                            ranstaxState.copy(
                                stacks = stacks.map { if (it == stack) savedStack else it },
                                stacksBeingEdited = stacksBeingEdited - stack
                            )
                        )
                    },
                    onDelete = {
                        onNewRanstaxState(
                            ranstaxState.copy(
                                stacks = stacks - stack,
                                stacksBeingEdited = stacksBeingEdited - stack
                            )
                        )
                    },
                )
            } else {
                EditableStack(stack) {
                    onNewRanstaxState(
                        ranstaxState.copy(
                            stacksBeingEdited = stacksBeingEdited + stack
                        )
                    )
                }
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

@Composable
fun ResetButton(onReset: () -> Unit) {
    Button(attrs = {
        style {
            onClick {
                if (window.confirm(
                        "Do you really want to reset all data?" +
                                " If you choose to reset, you will lose all current data."
                    )
                ) {
                    onReset()
                }
            }
        }
    }) {
        Text("🆕 Reset")
    }
}
