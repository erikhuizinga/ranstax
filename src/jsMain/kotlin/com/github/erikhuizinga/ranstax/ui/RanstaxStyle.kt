package com.github.erikhuizinga.ranstax.ui

import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSBuilder
import org.jetbrains.compose.web.css.CSSStyleRuleBuilder
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.div
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.flexFlow
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.flexShrink
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.minHeight
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.rowGap
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.css.textDecoration
import org.jetbrains.compose.web.css.times
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.width


object RanstaxStyle : StyleSheet() {
    //region Theme colors: https://www.canva.com/colors/color-palettes/random-market-finds/
    private val cream = Color("#F6F4E7")
    private val gray = Color("#7C847F")
    private val cognac = Color("#937965")
    private val kellyGreen = Color("#69914D")
    //endregion

    //region More colors
    private val shadow = rgba(0, 0, 0, 0.4)
    //endregion

    //region Dimensions
    private val smallSize = 2.px
    private val mediumSize = 8.px
    private val largeSize = 20.px

    private val borderRadiusSize = 2.px

    private val mediumFontSize = 1.em
    private val smallFontSize = mediumFontSize / 12 * 10
    private val largeFontSize = mediumFontSize * 2
    //endregion

    private val boxShadow = "$shadow 0px 2px 4px 0px"

    init {
        universal style {
            boxSizing("border-box")
            margin(0.px)
        }
        "html, body, #ranstax" style {
            height(100.percent)
            margin(0.px)
            padding(0.px)
            backgroundColor(cream)
            sansSerifFont()
            fontSize(mediumFontSize)
        }
        "button" style {
            backgroundColor(cognac)
            fontSize(mediumFontSize)
            headingFont()
            color(cream)
            roundBorder()
            property("box-shadow", boxShadow)
        }
        type("button") + attr("disabled") style {
            opacity(0.3)
        }
        "input" style {
            backgroundColor(cream)
            color(gray)
            sansSerifFont()
            roundBorder(LineStyle.Solid)
        }
        "h1, h2, h3, h4, h5, h6" style {
            headingFont()
        }
        "a" style {
            color(cognac)
            textDecoration("none")
        }
        "a:hover" {
            color(kellyGreen)
            textDecoration("underline")
        }
    }

    private fun CSSStyleRuleBuilder.titleFont() {
        fontFamily("Barriecito", "sans-serif")
    }

    private fun CSSStyleRuleBuilder.headingFont() {
        fontFamily("Carter One", "sans-serif")
    }

    private fun CSSStyleRuleBuilder.sansSerifFont() {
        fontFamily("Ubuntu", "sans-serif")
    }

    private fun CSSStyleRuleBuilder.roundBorder(lineStyle: LineStyle = LineStyle.None) {
        border {
            borderRadius(borderRadiusSize)
            width(2.px)
            color(cognac)
            style(lineStyle)
        }
    }

    val layout by style {
        flexListStyle(
            flexDirection = FlexDirection.Column,
            flexWrap = FlexWrap.Nowrap,
        )
        boxSizing("border-box")
        gap(largeSize)
        height(100.percent)
    }
    val column by style {
        flexListStyle(flexDirection = FlexDirection.Column)
    }
    val tightColumn by style {
        rowGap(smallSize)
    }
    val row by style {
        flexListStyle(
            flexDirection = FlexDirection.Row,
            alignItems = AlignItems.Baseline,
        )
    }
    val header by style {
        titleFont()
        fontSize(mediumFontSize * 4)
        color(cream)
        backgroundColor(kellyGreen)
        paddingTop(mediumSize)
        paddingBottom(mediumSize)
        paddingLeft(largeSize)
        paddingRight(largeSize)
        width(100.percent)
        property("box-shadow", boxShadow)
    }
    val app by style {
        padding(mediumSize)
        gap(largeSize)
        maxWidth(600.px)
    }
    val footer by style {
        rowGap(smallSize)
        textAlign("center")
        padding(largeSize)
    }
    val borderRadius by style {
        roundBorder()
    }
    val visibleBorder by style {
        border(1.px, LineStyle.Solid, gray)
    }
    val history by style {
        padding(mediumSize)
        maxHeight(20.vh)
        minHeight(14.em)
        overflowY("scroll")
    }
    val largeButton by style {
        padding(largeSize)
        fontSize(largeFontSize)
    }
    val mediumButton by style {
        padding(mediumSize)
        fontSize(mediumFontSize)
    }
    val smallFont by style {
        fontSize(smallFontSize)
    }

    init {
        group(className(header), className(footer)) style {
            flexGrow(0)
            flexShrink(0)
        }
    }

    val mainContent by style {
        flexGrow(1)
        paddingTop(0.px)
        paddingBottom(0.px)
        self + " *" style {
            maxWidth(100.percent)
        }
    }

    private fun CSSBuilder.flexListStyle(
        flexDirection: FlexDirection,
        alignItems: AlignItems = AlignItems.Normal,
        flexWrap: FlexWrap = FlexWrap.Wrap,
    ) {
        display(DisplayStyle.Flex)
        flexFlow(flexDirection, flexWrap)
        alignItems(alignItems)
        gap(rowGap = mediumSize, columnGap = mediumSize)
    }
}
