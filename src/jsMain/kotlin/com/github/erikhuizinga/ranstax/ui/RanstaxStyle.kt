package com.github.erikhuizinga.ranstax.ui

import org.jetbrains.compose.web.css.CSSBuilder
import org.jetbrains.compose.web.css.CSSStyleRuleBuilder
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.boxSizing
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.flexFlow
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.gap
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.marginBottom
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.minWidth
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.pt
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
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
    private val largeMargin = 16.px

    private val smallPadding = 2.px
    private val mediumPadding = 8.px
    private val largePadding = 16.px

    private val borderRadiusSize = 2.px

    private val smallFontSize = 0.85.em
    private val mediumFontSize = 1.em
    private val largeFontSize = 2.em
    //endregion

    private val boxShadow = "$shadow 0px 2px 4px 0px"

    init {
        universal style {
            boxSizing("border-box")
            margin(0.px)
        }
        "body" style {
            height(100.percent)
            backgroundColor(cream)
            sansSerifFont()
            fontSize(mediumFontSize)
        }
        "button" style {
            backgroundColor(cognac)
            fontSize(mediumFontSize)
            headerFont()
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
            headerFont()
        }
    }

    private fun CSSStyleRuleBuilder.titleFont() {
        fontFamily("Barriecito", "sans-serif")
    }

    private fun CSSStyleRuleBuilder.headerFont() {
        fontFamily("Carter One", "sans-serif")
    }

    private fun CSSStyleRuleBuilder.monospaceFont() {
        fontFamily("Ubuntu Mono", "monospace")
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
        flexListStyle(FlexDirection.Column)
        boxSizing("border-box")
        height(100.percent)
    }
    val column by style {
        flexListStyle(FlexDirection.Column)
    }
    val row by style {
        flexListStyle(FlexDirection.Row)
    }
    val header by style {
        titleFont()
        fontSize(64.pt)
        color(cream)
        backgroundColor(kellyGreen)
        padding(largePadding)
        paddingTop(largePadding)
        marginBottom(largeMargin)
        width(100.percent)
        property("box-shadow", boxShadow)
    }
    val app by style {
        padding(mediumPadding)
    }
    val borderRadius by style {
        roundBorder()
    }
    val visibleBorder by style {
        border(1.px, LineStyle.Solid, gray)
    }
    val history by style {
        padding(mediumPadding)
        monospaceFont()
        maxHeight(20.vh)
        minWidth(80.vw)
        overflowY("scroll")
    }
    val largeButton by style {
        padding(largePadding)
        fontSize(largeFontSize)
    }
    val mediumButton by style {
        padding(mediumPadding)
        fontSize(mediumFontSize)
    }
    val smallFont by style {
        fontSize(smallFontSize)
    }

    private fun CSSBuilder.flexListStyle(flexDirection: FlexDirection) {
        flexFlow(flexDirection, FlexWrap.Wrap)
        display(DisplayStyle.Flex)
        // alignItems(AlignItems.Normal)
        justifyContent(JustifyContent.SpaceBetween)
        gap(smallPadding, smallPadding)
    }
}
