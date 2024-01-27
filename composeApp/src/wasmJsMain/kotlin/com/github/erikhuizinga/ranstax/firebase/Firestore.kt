package com.github.erikhuizinga.ranstax.firebase

import kotlin.js.Promise

@Suppress("UnusedParameter")
@JsModule("firebase/firestore")
external object Firestore : JsAny {
    fun getFirestore(): FirebaseFirestore
    fun collection(firebaseFirestore: FirebaseFirestore, path: String): CollectionReference<DocumentData, DocumentData>
    fun getDocs(collectionReference: CollectionReference<*, *>): Promise<JsArray<DocumentReference>>
    fun doc(firebaseFirestore: FirebaseFirestore, path: String): DocumentReference
}

external class FirebaseFirestore : JsAny

external class DocumentReference : JsAny {
    val id: String
    val parent: CollectionReference<*, *>
    val path: String
    val type: JsAny
}

external class CollectionReference<T : JsAny, U : JsAny> : JsAny {
    val id: String
    val parent: DocumentReference?
    val path: String
    val type: JsAny
}

external class DocumentData : JsAny

external class DocumentSnapshot : JsAny {
    val exists: Boolean
    val id: String
    val ref: DocumentReference
    val data: JsAny
}
