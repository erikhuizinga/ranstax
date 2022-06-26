import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    var state: State by mutableStateOf(
        State(
            listOf(
                Stack(name = "North America", size = 181),
                Stack(name = "Oceania", size = 95),
                Stack(name = "Europe", size = 81),
            )
        )
    )

    renderComposable(rootElementId = "ranstax") {
        Div({ style { padding(25.px) } }) {
            H3 { Text("Stacks") }
            Div {
                state.stacks.forEach { (name, size) ->
                    Div {
                        Text("$name: $size")
                    }
                }
            }
        }
    }
}

