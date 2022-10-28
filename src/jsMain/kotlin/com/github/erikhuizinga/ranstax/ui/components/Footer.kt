package com.github.erikhuizinga.ranstax.ui.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLDivElement

@Composable
fun Footer(
    attrs: AttrBuilderContext<HTMLDivElement>? = null,
) {
    Column({ attrs?.invoke(this) }) {
        val emoji = setOf("💚", "🎲", "☕️", "🃏", "🍀", "👾", "🐈‍⬛", "😻", "🐾").random()
        SmallTextSpan("Created with $emoji️ by Erik Huizinga")
        SmallTextSpan("Ranstax is free and open source software")
        Span {
            val nbsp = '\u00A0'
            SmallTextSpan(
                "Please contribute by reporting issues, " +
                        "requesting features and by helping develop "
            )
            A("https://github.com/erikhuizinga/ranstax") {
                SmallTextSpan("💻${nbsp}Ranstax${nbsp}on${nbsp}GitHub")
            }
            SmallTextSpan(", or by ")
            A("https://paypal.me/erikhuizinga/") {
                SmallTextSpan("supporting${nbsp}me${nbsp}🙏")
            }
        }
    }
}
