package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import kotlin.math.min
import kotlin.random.Random

@Composable
fun Controls(
    ranstaxState: RanstaxState,
    onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit,
) {
    Column {
        DrawButtons(ranstaxState.isDrawButtonEnabled) { numberToDraw ->
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
    var newRanstaxState = ranstaxState
    val theNumToDraw = min(numberToDraw, ranstaxState.totalStackSize)
    val drawnStackNames = mutableListOf<String>()
    repeat(theNumToDraw) {
        var chosenIndex = Random.nextInt(newRanstaxState.totalStackSize)
        val stacks = newRanstaxState.stacks
        val chosenStack = stacks.first {
            chosenIndex -= it.size
            chosenIndex < 0
        }
        newRanstaxState = newRanstaxState.copy(
            allStacks = newRanstaxState.allStacks.mapKeys { (stack, _) ->
                if (stack == chosenStack) {
                    chosenStack.copy(size = chosenStack.size - 1)
                } else {
                    stack
                }
            },
        )
        drawnStackNames += chosenStack.name
    }
    newRanstaxState = newRanstaxState.copy(
        drawnStackNames = newRanstaxState.drawnStackNames + listOf(drawnStackNames)
    )
    onNewRanstaxStateTransform { newRanstaxState }
}
