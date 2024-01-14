package com.github.erikhuizinga.ranstax.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private const val HELLO_WORLD_COUNT = 100

@Composable
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text("Ranstax")
                }
            },
            bottomBar = {
                BottomAppBar {
                    Text("Developed by Erik Huizinga")
                }
            },
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {
                repeat(HELLO_WORLD_COUNT) {
                    item {
                        Text("Hello, World! $it")
                    }
                }
            }
        }
    }
}
