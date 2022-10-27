package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlinx.browser.document

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

    Column({ classes(RanstaxStyle.app) }) {
        Controls(ranstaxState, onNewRanstaxState)
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
