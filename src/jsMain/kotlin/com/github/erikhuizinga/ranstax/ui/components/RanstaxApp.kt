package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.domain.StackValidator
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.w3c.dom.HTMLDivElement

@Composable
fun RanstaxApp(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
    ranstaxState: RanstaxState,
    onNewRanstaxStateTransform: (RanstaxState.() -> RanstaxState) -> Unit,
) {
    document.onkeyup = { event ->
        if (!ranstaxState.isEditing && ranstaxState.canDraw && event.key in "1".."9") {
            onDraw(event.key.toInt(), ranstaxState, onNewRanstaxStateTransform)
            event.preventDefault()
        }
    }

    val onEditingChange = { newIsEditing: Boolean ->
        onNewRanstaxStateTransform { copy(isEditing = newIsEditing) }
    }

    Column({
        classes(RanstaxStyle.app)
        attrs?.invoke(this)
    }) {
        DrawControls(ranstaxState, onNewRanstaxStateTransform)
        History(ranstaxState) {
            onNewRanstaxStateTransform {
                copy(isMostRecentHistoryOnTop = !isMostRecentHistoryOnTop)
            }
        }
        StackList(ranstaxState, onNewRanstaxStateTransform, onEditingChange)
        NewStackInput(
            onNewStack = { newStack -> onNewRanstaxStateTransform { this + newStack } },
            onEditingChange = onEditingChange,
            stackValidator = StackValidator(ranstaxState.stacksNotBeingEdited.toSet()),
        )
        StateControls(
            onSaveStacks = { onNewRanstaxStateTransform { ranstaxState.saveStacks() } },
            canLoad = ranstaxState.hasSavedStacks,
            onLoad = { onNewRanstaxStateTransform { ranstaxState.loadStacks() } },
            onClearHistory = { onNewRanstaxStateTransform { ranstaxState.clearHistory() } },
            canClear = ranstaxState.hasStacks || ranstaxState.drawnStackNames.isNotEmpty(),
            onClear = { onNewRanstaxStateTransform { RanstaxState() } },
        )
    }
}
