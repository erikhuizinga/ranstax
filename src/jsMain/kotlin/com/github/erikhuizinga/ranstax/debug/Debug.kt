package com.github.erikhuizinga.ranstax.debug

import kotlinx.browser.document
import org.w3c.dom.url.URLSearchParams

internal val DEBUG = URLSearchParams(document.location?.search).get("debug").toBoolean()

internal fun log(m: String) {
    if (DEBUG) println(m)
}
