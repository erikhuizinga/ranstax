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
                repeat(100) {
                    item {
                        Text("Hello, World! $it")
                    }
                }
            }
        }
    }
}
