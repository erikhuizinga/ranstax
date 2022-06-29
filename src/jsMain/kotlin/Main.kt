import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.size
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

typealias ComposableLambda = @Composable () -> Unit

fun main() {
    renderComposable(rootElementId = "ranstax") {
        val stacks = remember { mutableStateListOf<Stack>() }
        Div({ style { padding(24.px) } }) {
            H3 { Text("Stacks") }
            Div({ style { paddingVertical(8.px) } }) {
                NewStackInput(
                    isValidName = { stacks.none { it.name == this } },
                    onNewStack = { stacks += it },
                )
            }
            Div({ style { paddingVertical(8.px) } }) {
                stacks.map<Stack, ComposableLambda> { (name, size) ->
                    { Text("$name: $size") }
                }.ifEmpty { listOf<ComposableLambda> { Text("no stacks, add one here 👆") } }.forEach { Div { it() } }
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

    Span({ style { paddingRight(4.px) } }) {
        TextInput(value = name) {
            placeholder("name")
            onInput { name = it.value }
        }
    }
    Span({ style { paddingHorizontal(4.px) } }) {
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
    Span({ style { paddingHorizontal(4.px) } }) {
        Button(attrs = {
            onClick {
                val newStack = Stack(name, size)
                if (newStack.isValid && name.isValidName()) {
                    name = defaultName
                    size = defaultSize
                    onNewStack(newStack)
                } else {

                }
            }
        }) {
            Text("➕")
        }
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
