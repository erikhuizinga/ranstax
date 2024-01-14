package com.github.erikhuizinga.ranstax

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.github.erikhuizinga.ranstax.firebase.initializeFirebaseForRanstax
import com.github.erikhuizinga.ranstax.ui.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initializeFirebaseForRanstax()
    CanvasBasedWindow(canvasElementId = "ComposeTarget") { App() }
}
