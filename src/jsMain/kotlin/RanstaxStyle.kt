import org.jetbrains.compose.web.css.AlignItems
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
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.flexFlow
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.minWidth
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.pt
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.whiteSpace
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
    private val smallPadding = 2.px
    private val mediumPadding = 8.px
    private val largePadding = 16.px
    private val borderRadiusSize = 2.px
    //endregion

    private val boxShadow = "$shadow 0px 2px 4px 0px"

    init {
        universal style {
            boxSizing("border-box")
            margin(0.px)
        }
        "html, body" style {
            height(100.percent)
            backgroundColor(cream)
        }
        "button" style {
            backgroundColor(cognac)
            fontSize(12.pt)
            property("text-transform", "uppercase")
            fontWeight(600)
            border(style = LineStyle.None)
            property("box-shadow", boxShadow)
        }
        "input" style {
            backgroundColor(cream)
        }
    }

    val layout by style {
        display(DisplayStyle.Flex)
        flexFlow(FlexDirection.Column, FlexWrap.Wrap)
        alignItems(AlignItems.Center)
        height(100.percent)
    }
    val column by style {
        display(DisplayStyle.Flex)
        flexFlow(FlexDirection.Column, FlexWrap.Wrap)
        alignItems(AlignItems.Normal)
    }
    val columnHeader by style {
        paddingBottom(smallPadding)
    }
    val columnFooter by style {
        paddingTop(smallPadding)
    }
    val row by style {
        display(DisplayStyle.Flex)
        flexFlow(FlexDirection.Row, FlexWrap.Wrap)
        alignItems(AlignItems.Normal)
    }
    val rowHeader by style {
        paddingRight(smallPadding)
    }
    val rowFooter by style {
        paddingLeft(smallPadding)
    }
    val header by style {
        whiteSpace("pre")
        fontFamily("monospace")
        fontWeight(900)
        color(cream)
        backgroundColor(kellyGreen)
        padding(largePadding)
        paddingTop(largePadding - 1.em)
        width(100.vw)
        property("box-shadow", boxShadow)
    }
    val app by style {
        padding(mediumPadding)
    }
    val borderRadius by style {
        borderRadius(borderRadiusSize)
        border(style = LineStyle.None)
    }
    val visibleBorder by style {
        border(1.px, LineStyle.Solid, gray)
    }
    val history by style {
        padding(mediumPadding)
        fontFamily("monospace")
        maxHeight(20.vh)
        minWidth(80.vw)
        overflowY("scroll")
    }
}
