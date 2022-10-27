package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlin.math.min
import kotlin.random.Random
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Div

@Composable
fun RanstaxApp(ranstaxState: RanstaxState, onNewRanstaxState: (RanstaxState) -> Unit) {
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
        Column {
            DrawButton(ranstaxState) { onDraw(1, ranstaxState, onNewRanstaxState) }
            InfoMessage(ranstaxState)
            History(ranstaxState)
            StackList(ranstaxState, onNewRanstaxState, onEditingChange)

            NewStackInput(
                onNewStack = {
                    onNewRanstaxState(ranstaxState.copy(stacks = stacks + it))
                },
                onEditingChange = onEditingChange,
                ranstaxState = ranstaxState,
            )
            Clear { onNewRanstaxState(RanstaxState()) }
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
