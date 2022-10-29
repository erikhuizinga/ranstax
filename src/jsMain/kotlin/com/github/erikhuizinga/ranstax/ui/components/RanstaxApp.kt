package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.data.RanstaxState
import com.github.erikhuizinga.ranstax.domain.NewStackValidatorImpl
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
    val stacks = ranstaxState.stacks

    document.onkeyup = { event ->
        if (!ranstaxState.isEditing && ranstaxState.isDrawButtonEnabled && event.key in "1".."9") {
            onDraw(event.key.toInt(), ranstaxState, onNewRanstaxStateTransform)
            event.preventDefault()
        }
    }

    val onEditingChange = { isEditing: Boolean ->
        onNewRanstaxStateTransform { copy(isEditing = isEditing) }
    }

    Column({
        classes(RanstaxStyle.app)
        attrs?.invoke(this)
    }) {
        Controls(ranstaxState, onNewRanstaxStateTransform)
        History(ranstaxState)
        StackList(ranstaxState, onNewRanstaxStateTransform, onEditingChange)
        NewStackInput(
            onNewStack = {
                onNewRanstaxStateTransform { copy(stacks = stacks + it) }
            },
            onEditingChange = onEditingChange,
            stackValidator = NewStackValidatorImpl(ranstaxState),
        )
        ClearButtonSection(
            ranstaxState.hasStacks || ranstaxState.drawnStackNames.isNotEmpty()
        ) { onNewRanstaxStateTransform { RanstaxState() } }
    }
}
