package com.github.erikhuizinga.ranstax.dev

import kotlinx.browser.document
import org.w3c.dom.url.URLSearchParams

val DEV = URLSearchParams(document.location?.search).get("dev").toBoolean()