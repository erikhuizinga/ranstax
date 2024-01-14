package com.github.erikhuizinga.ranstax

import kotlinx.browser.window

private val isDev get() = window.location.hostname == "localhost"

fun devPrintln(message: Any?) {
    if (isDev) println(message)
}
