import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.web.events.SyntheticEvent
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.flexFlow
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.overflowX
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.whiteSpace
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.Small
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLDivElement
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
        val northAmericaStack = Stack("North America", 180)
        val europeStack = Stack("Europe", 81)
        val oceaniaStack = Stack("Oceania", 95)
        val stacks = listOf(northAmericaStack, europeStack, oceaniaStack)
        val stacksBeingEdited: List<Stack> = listOf(/* northAmericaStack */)
        storeRanstaxState(RanstaxState(stacks, stacksBeingEdited))
    }
}

private const val RANSTAX_STATE_KEY = "RanstaxState"

private fun storeRanstaxState(ranstaxState: RanstaxState) {
    localStorage[RANSTAX_STATE_KEY] = Json.encodeToString(ranstaxState)
}

private fun loadRanstaxState(): RanstaxState =
    localStorage[RANSTAX_STATE_KEY]?.let(Json.Default::decodeFromString) ?: RanstaxState()

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
        flexFlow(FlexDirection.Column, FlexWrap.Wrap)
        alignItems(AlignItems.Center)
        margin(0.px)
        height(100.percent)
    }
    val column by style {
        display(DisplayStyle.Flex)
        flexFlow(FlexDirection.Column, FlexWrap.Wrap)
        alignItems(AlignItems.Normal)
    }
    val row by style {
        display(DisplayStyle.Flex)
        flexFlow(FlexDirection.Row, FlexWrap.Wrap)
        alignItems(AlignItems.Normal)
    }
    val columnContainer by style {
        paddingTop(2.px)
        paddingBottom(2.px)
    }
    val rowContainer by style {
        paddingLeft(2.px)
        paddingRight(2.px)
    }
    val history by style {
        fontFamily("monospace")
        border(1.px, LineStyle.Solid, Color.lightgray)
        borderRadius(2.px)
        maxHeight(16.em)
        overflowY("scroll")
        padding(8.px)
    }
}

@Composable
fun Layout(vararg composables: @Composable () -> Unit) {
    Div({ style { classes(RanstaxStyle.layout) } }) {
        Column(*composables)
    }
}

@Composable
fun ColumnContainer(child: @Composable () -> Unit) {
    Container({ style { classes(RanstaxStyle.columnContainer) } }, child)
}

@Composable
fun RowContainer(child: @Composable () -> Unit) {
    Container({ style { classes(RanstaxStyle.rowContainer) } }, child)
}

@Composable
fun Container(attrs: AttrBuilderContext<HTMLDivElement>? = null, child: @Composable () -> Unit) {
    Div(attrs) { child() }
}

@Composable
fun RanstaxHeader() {
    Div({
        style {
            fontFamily("monospace")
            whiteSpace("pre")
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
    val isEditing: Boolean = false,
) {
    init {
        require(stacks.containsAll(stacksBeingEdited)) {
            "stacks (${stacks.joinToString()}) must contain all stacksBeingEdited (${stacksBeingEdited.joinToString()}). Stacks not in stacks: " + (stacksBeingEdited - stacks.toSet()).joinToString()
        }
    }

    val isDrawButtonEnabled = stacks.sumOf { it.size } > 0 && stacksBeingEdited.isEmpty()
}

@Composable
private fun RanstaxApp(ranstaxState: RanstaxState, onNewRanstaxState: (RanstaxState) -> Unit) {
    val stacks = ranstaxState.stacks

    document.onkeyup = { event ->
        if (!ranstaxState.isEditing && ranstaxState.isDrawButtonEnabled && event.key in "0".."9") {
            onDraw(event.key.toInt(), ranstaxState, onNewRanstaxState)
            event.preventDefault()
        }
    }

    val onEditingChange = { isEditing: Boolean ->
        onNewRanstaxState(ranstaxState.copy(isEditing = isEditing))
    }
    Column(
        { DrawButton(ranstaxState) { onDraw(1, ranstaxState, onNewRanstaxState) } },
        { History(ranstaxState) },
        { StackList(ranstaxState, onNewRanstaxState, onEditingChange) },
        {
            NewStackInput(
                isValidName = { stacks.none { it.name == trim() } },
                onNewStack = {
                    onNewRanstaxState(ranstaxState.copy(stacks = stacks + it))
                },
                onEditingChange = onEditingChange,
            )
        },
        { Reset { onNewRanstaxState(RanstaxState()) } },
    )
}

private fun onDraw(
    numToDraw: Int,
    ranstaxState: RanstaxState,
    onNewRanstaxState: (RanstaxState) -> Unit,
) {
    var newRanstaxState = ranstaxState
    val theNumToDraw = min(numToDraw, ranstaxState.stacks.sumOf { it.size })
    repeat(theNumToDraw) {
        val stacks = newRanstaxState.stacks
        var chosenIndex = Random.nextInt(stacks.sumOf { it.size })
        val chosenStack = stacks.first {
            chosenIndex -= it.size
            chosenIndex < 0
        }
        newRanstaxState = newRanstaxState.copy(
            stacks = stacks.map {
                if (it == chosenStack) {
                    chosenStack.copy(size = chosenStack.size - 1)
                } else {
                    it
                }
            },
            lastDrawnStackNames = newRanstaxState.lastDrawnStackNames + chosenStack.name,
        )
    }
    onNewRanstaxState(newRanstaxState)
}

@Composable
fun Column(vararg composables: @Composable () -> Unit) {
    Div({ style { classes(RanstaxStyle.column) } }) {
        composables.forEach { ColumnContainer(it) }
    }
}

@Composable
fun Column(composables: List<@Composable () -> Unit>) {
    Column(*composables.toTypedArray())
}

@Composable
fun Row(vararg composables: @Composable () -> Unit) {
    Div({ style { classes(RanstaxStyle.row) } }) {
        composables.forEach { RowContainer(it) }
    }
}

@Composable
private fun DrawButton(
    ranstaxState: RanstaxState,
    onDraw: () -> Unit,
) {
    Row(
        {
            Button({
                style {
                    if (ranstaxState.isDrawButtonEnabled) onClick {
                        onDraw()
                    } else {
                        disabled()
                    }
                }
            }
            ) {
                H3 {
                    Text("DRAW")
                }
            }
        },
        {
            Small {
                Text("ℹ️ press any number key to draw that many items")
            }
        },
    )
}

@Composable
//TODO Add user action to history
private fun History(ranstaxState: RanstaxState) {
    val (stacks, _, lastDrawnStackNames) = ranstaxState
    if (lastDrawnStackNames.isEmpty()) {
        if (ranstaxState.isDrawButtonEnabled) {
            Text(
                "👆 Draw to start history"
            )
        }
    } else {
        H3 {
            Text("📜 History")
        }

        Div({ style { classes(RanstaxStyle.history) } }) {
            DisposableEffect(lastDrawnStackNames.size) {
                scopeElement.apply { scrollTop = scrollHeight.toDouble() }
                onDispose {}
            }

            val indexTemplate = "\$index"
            val nameTemplate = "\$name"
            val indexedNameTemplate = "$indexTemplate: $nameTemplate"
            val indexLength =
                ceil(log10((stacks.sumOf { it.size } + lastDrawnStackNames.size + 1).toDouble())).roundToInt()
            lastDrawnStackNames.mapIndexed { index, lastDrawnStackName ->
                val indexString = (index + 1).toString()
                indexedNameTemplate.replace(
                    indexTemplate,
                    "0".repeat(indexLength - indexString.length) + indexString
                ).replace(nameTemplate, lastDrawnStackName)
            }.forEach {
                Div {
                    Text(it)
                }
            }
        }
    }
}

@Composable
private fun StackList(
    ranstaxState: RanstaxState,
    onNewRanstaxState: (RanstaxState) -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    val stacks = ranstaxState.stacks
    if (stacks.isEmpty()) {
        Text("No stacks, add some new ones")
        return
    }
    H3 {
        Text("📚 Stacks")
    }
    val stacksBeingEdited = ranstaxState.stacksBeingEdited
    Column(
        stacks.map<Stack, @Composable () -> Unit> { stack ->
            {
                if (stack in stacksBeingEdited) {
                    StackEditor(
                        currentStack = stack,
                        isValidName = {
                            val trimmedName = trim()
                            stacksBeingEdited.any { it.name == trimmedName } || stacks.none { it.name == trimmedName }
                        },
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
                        onEditingChange = onEditingChange,
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
    )
}

@Composable
private fun NewStackInput(
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
        onNewStack(stack.trimmedName())
        name = defaultName
        size = defaultSize
    }

    H3 {
        Text("🆕 New stack")
    }
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
            onEditingChange = onEditingChange,
        )
    }
    Span {
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

@Composable
private fun StackInput(
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
    TextInput(value = stack.name) {
        placeholder("enter a name")
        onInput { onInput(stack.copy(name = it.value)) }
        onKeyUp(submitListener)
        onFocus { onEditingChange(true) }
        onBlur { onEditingChange(false) }
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
            onFocus { onEditingChange(true) }
            onBlur { onEditingChange(false) }
        }
    }
}

@Composable
private fun EditableStack(
    stack: Stack,
    onEdit: () -> Unit,
) {
    Row(
        {
            Button({ onClick { onEdit() } }) {
                Text("✏️")
            }
        },
        {
            Span {
                Stack(stack)
            }
        },
    )
}

@Composable
private fun Stack(stack: Stack) {
    Text("${stack.name}: ${stack.size}")
}

private fun Stack.trimmedName() = copy(name = this.name.trim())

@Composable
private fun StackEditor(
    currentStack: Stack,
    isValidName: String.() -> Boolean,
    onSave: (savedStack: Stack) -> Unit,
    onDelete: () -> Unit,
    onEditingChange: (isEditing: Boolean) -> Unit,
) {
    var stack by remember { mutableStateOf(currentStack) }
    fun Stack.validate() = isValid && name.isValidName()
    Row(
        {
            Button({
                if (stack.validate()) onClick {
                    onSave(stack.trimmedName())
                } else {
                    disabled()
                }
            }) {
                Text("💾")
            }
        },
        {
            Button({ onClick { onDelete() } }) {
                Text("🗑")
            }
        },
        {
            StackInput(
                stack = stack,
                onInput = { stack = it },
                onSubmit = {
                    stack.takeIf { it.validate() }?.run(Stack::trimmedName)?.let(onSave)
                },
                onEditingChange = onEditingChange
            )
        },
    )
}

@Composable
fun Reset(onReset: () -> Unit) {
    H3 {
        Text("🔁 Reset app")
    }
    Button({
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
        H3 {
            Text("RESET")
        }
    }
}
