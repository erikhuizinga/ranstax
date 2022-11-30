package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import kotlin.js.Date
import kotlin.math.min
import kotlin.random.Random

@Composable
fun DrawControls(
    ranstaxState: RanstaxState,
    onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit,
) {
    Column {
        DrawButtons(ranstaxState.canDraw) { numberToDraw ->
            onDraw(numberToDraw, ranstaxState, onNewRanstaxStateTransform)
        }
        InfoMessage(ranstaxState)
    }
}

fun onDraw(
    numberToDraw: Int,
    ranstaxState: RanstaxState,
    onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit,
) {
    if ((Date.now() - ranstaxState.timeOfLastDraw) < 200) return

    var newRanstaxState = ranstaxState
    val theNumToDraw = min(numberToDraw, ranstaxState.totalStackSize)
    val drawnStackNames = mutableListOf<String>()

    repeat(theNumToDraw) {
        var chosenIndex = Random.nextInt(newRanstaxState.totalStackSize)
        val (drawnId, drawnStack) = newRanstaxState.stateStacks.first {
            chosenIndex -= it.stack.size
            chosenIndex < 0
        }
        newRanstaxState = newRanstaxState.replace(drawnId, drawnStack.run { copy(size = size - 1) })
        drawnStackNames += drawnStack.name
    }
    newRanstaxState = newRanstaxState.copy(
        drawnStackNames = newRanstaxState.drawnStackNames + listOf(drawnStackNames),
        timeOfLastDraw = Date.now(),
    )
    onNewRanstaxStateTransform { newRanstaxState }
}
