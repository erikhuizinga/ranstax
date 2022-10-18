package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import com.github.erikhuizinga.ranstax.ui.RanstaxStyle
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

interface ListScope {
    fun item(content: @Composable () -> Unit)
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

@Composable
private fun RowHeader(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.rowHeader) }) {
        content()
    }
}

@Composable
private fun RowElement(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.rowHeader, RanstaxStyle.rowFooter) }) {
        content()
    }
}

@Composable
private fun RowFooter(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.rowFooter) }) {
        content()
    }
}

@Composable
private fun ColumnHeader(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.columnHeader) }) {
        content()
    }
}

@Composable
private fun ColumnElement(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.columnHeader, RanstaxStyle.columnFooter) }) {
        content()
    }
}

@Composable
private fun ColumnFooter(content: @Composable () -> Unit) {
    Div({ classes(RanstaxStyle.columnFooter) }) {
        content()
    }
}

private class HeaderItemFooterListScope : ListScope {
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
