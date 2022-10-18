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
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.NumberInput
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.EventTarget
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.get
import org.w3c.dom.set
import org.w3c.dom.url.URLSearchParams

private val debug = URLSearchParams(document.location?.search).get("debug").toBoolean()

private fun log(m: String) {
    if (debug) println(m)
}

fun main() {
    if (debug) setupDebugStacks()

    renderComposable(rootElementId = "ranstax") {
        Style(RanstaxStyle)
        var ranstaxState by remember { mutableStateOf(loadRanstaxState()) }
        Layout {
            RanstaxHeader()
            RanstaxApp(ranstaxState) { newRanstaxState ->
                storeRanstaxState(newRanstaxState)
                ranstaxState = newRanstaxState
            }
        }
    }
}

private fun setupDebugStacks() {
    if (loadRanstaxState().stacks.sumOf { it.size } == 0) {
        val northAmericaStack = Stack("North America", 180)
        val europeStack = Stack("Europe", 81)
        val oceaniaStack = Stack("Oceania", 95)
        val asiaStack = Stack("Asia", 90)
        val stacks = listOf(northAmericaStack, europeStack, oceaniaStack, asiaStack)
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

@Composable
fun Layout(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.layout) }) {
        content()
    }
}

@Composable
fun ColumnHeader(child: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.columnHeader) }) {
        child()
    }
}

@Composable
fun ColumnElement(child: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.columnHeader, RanstaxStyle.columnFooter) }) {
        child()
    }
}

@Composable
fun ColumnFooter(child: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.columnFooter) }) {
        child()
    }
}

@Composable
fun RowHeader(child: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.rowHeader) }) {
        child()
    }
}

@Composable
fun RowElement(child: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.rowHeader, RanstaxStyle.rowFooter) }) {
        child()
    }
}

@Composable
fun RowFooter(child: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.rowFooter) }) {
        child()
    }
}

@Composable
fun RanstaxHeader() {
    Div({ classes(RanstaxStyle.header) }) {
        Text("Ranstax")
    }
}

@Serializable
private data class RanstaxState(
    val stacks: List<Stack> = emptyList(),
    val stacksBeingEdited: List<Stack> = emptyList(),
    val drawnStackNames: List<List<String>> = emptyList(),
    val isEditing: Boolean = false,
) {
    init {
        require(stacks.containsAll(stacksBeingEdited)) {
            "stacks (${stacks.joinToString()}) must contain all " + "stacksBeingEdited (${stacksBeingEdited.joinToString()})." + " Stacks not in stacks: " + (stacksBeingEdited - stacks.toSet()).joinToString()
        }
    }

    val hasStacks = stacks.isNotEmpty()
    val totalStackSize = stacks.sumOf { it.size }
    val areAllStacksEmpty = totalStackSize == 0
    val isDrawButtonEnabled = !areAllStacksEmpty && stacksBeingEdited.isEmpty()
}

@Composable
private fun RanstaxApp(ranstaxState: RanstaxState, onNewRanstaxState: (RanstaxState) -> Unit) {
    val stacks = ranstaxState.stacks

    document.onkeyup = { event ->
        if (!ranstaxState.isEditing && ranstaxState.isDrawButtonEnabled && event.key in "1".."9") {
            onDraw(event.key.toInt(), ranstaxState, onNewRanstaxState)
            event.preventDefault()
        }
    }

    val onEditingChange = { isEditing: Boolean ->
        onNewRanstaxState(ranstaxState.copy(isEditing = isEditing))
    }
    Div({ classes(RanstaxStyle.app) }) {
        Column({ classes(RanstaxStyle.mediumElementPadding) }) {
            item { DrawButton(ranstaxState) { onDraw(1, ranstaxState, onNewRanstaxState) } }
            item { InfoMessage(ranstaxState) }
            item { History(ranstaxState) }
            item { StackList(ranstaxState, onNewRanstaxState, onEditingChange) }
            item {
                NewStackInput(
                    isValidName = { stacks.none { it.name == trim() } },
                    onNewStack = {
                        onNewRanstaxState(ranstaxState.copy(stacks = stacks + it))
                    },
                    onEditingChange = onEditingChange,
                )
            }
            item { Clear { onNewRanstaxState(RanstaxState()) } }
        }
    }
}

private fun onDraw(
    numToDraw: Int,
    ranstaxState: RanstaxState,
    onNewRanstaxState: (RanstaxState) -> Unit,
) {
    var newRanstaxState = ranstaxState
    val theNumToDraw = min(numToDraw, ranstaxState.totalStackSize)
    val drawnStackNames = mutableListOf<String>()
    repeat(theNumToDraw) {
        var chosenIndex = Random.nextInt(newRanstaxState.totalStackSize)
        val stacks = newRanstaxState.stacks
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
        )
        drawnStackNames += chosenStack.name
    }
    newRanstaxState = newRanstaxState.copy(
        drawnStackNames = newRanstaxState.drawnStackNames + listOf(drawnStackNames)
    )
    onNewRanstaxState(newRanstaxState)
}

@Composable
fun Column(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    content: @Composable ListScope.() -> Unit,
) {
    HeaderItemsFooterList(
        attrs = {
            classes(RanstaxStyle.column)
            attrs?.invoke(this)
        },
        Header = { ColumnHeader(it) },
        Element = { ColumnElement(it) },
        Footer = { ColumnFooter(it) },
        content = content,
    )
}

interface ListScope {
    fun item(content: @Composable () -> Unit)
}

internal class HeaderItemFooterListScope internal constructor() : ListScope {
    private val privateItems = mutableListOf<@Composable () -> Unit>()
    val items: List<@Composable () -> Unit> = privateItems
    override fun item(content: @Composable () -> Unit) = privateItems.plusAssign(content)
}

@Composable
private fun HeaderItemsFooterList(
    attrs: AttrBuilderContext<HTMLDivElement>,
    Header: @Composable (@Composable () -> Unit) -> Unit,
    Element: @Composable (@Composable () -> Unit) -> Unit,
    Footer: @Composable (@Composable () -> Unit) -> Unit,
    content: @Composable ListScope.() -> Unit,
) {
    val items = HeaderItemFooterListScope()
        .apply { content() }
        .items
        .takeIf { it.isNotEmpty() }
        ?: return

    Div({
        classes(RanstaxStyle.smallElementPadding)
        attrs()
    }) {
        when (items.size) {
            1 -> items[0]()
            else -> {
                Header(items[0])
                val tail = items.drop(1)
                tail.dropLast(1).forEach { Element(it) }
                Footer(items.last())
            }
        }
    }
}

@Composable
fun Row(content: @Composable ListScope.() -> Unit) {
    HeaderItemsFooterList(
        attrs = { classes(RanstaxStyle.row) },
        Header = { RowHeader(it) },
        Element = { RowElement(it) },
        Footer = { RowFooter(it) },
        content = content,
    )
}

@Composable
private fun DrawButton(
    ranstaxState: RanstaxState,
    onDraw: () -> Unit,
) {
    Button({
        classes(RanstaxStyle.largeButton)
        if (ranstaxState.isDrawButtonEnabled) onClick {
            onDraw()
        } else {
            disabled()
        }
    }) {
        Text("Draw")
    }
}

@Composable
private fun InfoMessage(ranstaxState: RanstaxState) {
    Text(
        when (ranstaxState.hasStacks) {
            true -> when (ranstaxState.areAllStacksEmpty) {
                true -> "🫥 Nothing left to draw, stacks are empty"
                false -> when (ranstaxState.stacksBeingEdited.isEmpty()) {
                    true -> "ℹ️ Press any number key to draw that many items"
                    false -> "⚠️ Finish editing all stacks to enable drawing"
                }
            }

            false -> "No stacks, add some new ones"
        }
    )
}

@Composable
private fun History(ranstaxState: RanstaxState) {
    val drawnStackNames = ranstaxState.drawnStackNames
    H3 {
        Text("📜 History")
    }
    if (drawnStackNames.isEmpty()) {
        Text("Draw to start history")
        return
    }
    Div({
        classes(
            RanstaxStyle.history,
            RanstaxStyle.borderRadius,
            RanstaxStyle.visibleBorder,
        )
    }) {
        DisposableEffect(drawnStackNames.size) {
            fun scrollToEnd() {
                scopeElement.apply { scrollTop = scrollHeight.toDouble() }
            }
            window.onresize = { scrollToEnd() }
            scrollToEnd()
            onDispose {}
        }

        val indexTemplate = "\$index"
        val nameTemplate = "\$name"
        val indexedNameTemplate = "$indexTemplate: $nameTemplate"
        val indexLength =
            ceil(log10((ranstaxState.totalStackSize + drawnStackNames.sumOf { it.size } + 1).toDouble())).roundToInt()
        var index = 0
        drawnStackNames.forEach { drawActionStackNames ->
            Div {
                Text("Drew ${drawActionStackNames.size} 👇")
            }
            drawActionStackNames.map { stackName ->
                val indexString = (++index).toString()
                indexedNameTemplate.replace(
                    indexTemplate, "0".repeat(indexLength - indexString.length) + indexString
                ).replace(nameTemplate, stackName)
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
        return
    }
    H3 {
        Text("📚 Stacks")
    }
    val stacksBeingEdited = ranstaxState.stacksBeingEdited
    Column {
        stacks.forEach { stack ->
            item {
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
                                    stacksBeingEdited = stacksBeingEdited - stack,
                                )
                            )
                        },
                        onDelete = {
                            onNewRanstaxState(
                                ranstaxState.copy(
                                    stacks = stacks - stack,
                                    stacksBeingEdited = stacksBeingEdited - stack,
                                )
                            )
                        },
                        onEditingChange = onEditingChange,
                    )
                } else {
                    EditableStack(stack) {
                        onNewRanstaxState(
                            ranstaxState.copy(stacksBeingEdited = stacksBeingEdited + stack)
                        )
                    }
                }
            }
        }
    }
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
    Row {
        item {
            TextInput(value = stack.name) {
                placeholder("enter a name")
                onInput { onInput(stack.copy(name = it.value)) }
                onKeyUp(submitListener)
                onFocus { onEditingChange(true) }
                onBlur { onEditingChange(false) }
            }
        }
        item {
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
}

@Composable
private fun EditableStack(
    stack: Stack,
    onEdit: () -> Unit,
) {
    Row {
        item {
            Button({ onClick { onEdit() } }) {
                Text("✏️")
            }
        }
        item {
            Stack(stack)
        }
    }
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
    Row {
        item {
            Button({
                if (stack.validate()) onClick {
                    onSave(stack.trimmedName())
                } else {
                    disabled()
                }
            }) {
                Text("💾")
            }
        }
        item {
            Button({ onClick { onDelete() } }) {
                Text("🗑")
            }
        }
        item {
            StackInput(
                stack = stack,
                onInput = { stack = it },
                onSubmit = { stack.takeIf { it.validate() }?.run(Stack::trimmedName)?.let(onSave) },
                onEditingChange = onEditingChange
            )
        }
    }
}

@Composable
fun Clear(onReset: () -> Unit) {
    H3 {
        Text("🔁 Clear everything")
    }
    Button({
        classes(RanstaxStyle.mediumButton)
        onClick {
            if (window.confirm(
                    "Do you really want to clear all data?" +
                            " If you choose to clear, you will lose all current data."
                )
            ) {
                onReset()
            }
        }
    }) {
        Text("Clear")
    }
}
