package com.github.erikhuizinga.ranstax.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.erikhuizinga.ranstax.devPrintln
import com.github.erikhuizinga.ranstax.firebase.DocumentReference
import com.github.erikhuizinga.ranstax.firebase.Firestore
import kotlinx.coroutines.asDeferred

@Composable
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text("Ranstax")
                }
            },
            content = { innerPadding ->
                Box(
                    Modifier.padding(innerPadding)
                ) {
                    devPrintln("Box content")
                    DocumentContent()
                }
            },
            bottomBar = {
                BottomAppBar {
                    Text("Developed by Erik Huizinga")
                }
            },
        )
    }
}

@Composable
private fun DocumentContent() {
    val firebaseFirestore = Firestore.getFirestore()
    devPrintln("firebaseFirestore")
    firebaseFirestore.propertyNames.forEachIndexed { index, key ->
        devPrintln("key[$index] = $key")
    }

    val collectionReference = Firestore.collection(firebaseFirestore, "stacks")
    devPrintln("collectionReference")
    collectionReference.propertyNames.forEachIndexed { index, key ->
        devPrintln("key[$index] = $key")
    }

    val documentReference = Firestore.doc(firebaseFirestore, "/stacks/wingspan base + ss")
    devPrintln("documentReference")
    documentReference.propertyNames.forEachIndexed { index, key ->
        devPrintln("key[$index] = $key")
    }
    devPrintln("type = ${documentReference.type}")
    devPrintln("id = ${documentReference.id}")
    devPrintln("path = ${documentReference.path}")
    devPrintln("parent = ${documentReference.parent}")

    val documentReferencesPromise = Firestore.getDocs(collectionReference)
    devPrintln("documentReferencesPromise")
    documentReferencesPromise.propertyNames.forEachIndexed { index, key ->
        devPrintln("key[$index] = $key")
    }

    val documentReferencesDeferred = documentReferencesPromise.asDeferred<JsArray<DocumentReference>>()
    var loading by remember { mutableStateOf(documentReferencesDeferred.isCompleted) }
    var documentReferences by remember { mutableStateOf(emptyList<DocumentReference>()) }

    if (loading) {
        devPrintln("Text(\"Loading...\")")
        Text("Loading...")
    } else {
        devPrintln("LazyColumn")
        LazyColumn {
            devPrintln("there are ${documentReferences.size} document references")
            if (documentReferences.isEmpty()) {
                item {
                    Text("No document references.")
                }
            } else {
                devPrintln("documentReferences = ${documentReferences.joinToString()}")
                item {
                    Text("Document references:")
                }
                items(documentReferences) { documentReference ->
                    Text("documentReference = $documentReference")
                }
                item {
                    Text("End of document references.")
                }
            }
        }
    }
    LaunchedEffect(documentReferencesDeferred) {
        devPrintln("LaunchedEffect")
        val documentReferenceJsArray = documentReferencesDeferred.await()
        documentReferences = buildList<DocumentReference> {
            for (i in 0 until documentReferenceJsArray.length) {
                documentReferenceJsArray[i]?.let(::add)
            }
        }
        devPrintln("documentReferences = ${documentReferences.joinToString()}")
        loading = false
    }
}

private fun keys(@Suppress("UnusedParameter") jsAny: JsAny): JsArray<JsString> =
    js("Object.getOwnPropertyNames(jsAny)")

private val JsAny.propertyNames
    get() = keys(this).run {
        List(length) { this[it].toString() }
    }
