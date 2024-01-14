package com.github.erikhuizinga.ranstax.firebase

import com.github.erikhuizinga.ranstax.devPrintln

@JsModule("firebase/app")
external object Firebase : JsAny {
    fun initializeApp(@Suppress("UnusedParameter") firebaseConfig: JsAny): FirebaseApp
}

external class FirebaseApp : JsAny {
    val name: String
    val options: FirebaseOptions
}

external class FirebaseOptions : JsAny {
    val applicationId: String?
    val apiKey: String
    val databaseUrl: String?
    val gaTrackingId: String?
    val storageBucket: String?
    val projectId: String?
    val gcmSenderId: String?
    val authDomain: String?
}

@Suppress("MaxLineLength")
fun createRanstaxFirebaseConfig(): JsAny = js(
    "({apiKey: \"AIzaSyAOc-w28EX8-sl9-uj1z7yytrtsQdta_bQ\", authDomain: \"ranstax1337.firebaseapp.com\", projectId: \"ranstax1337\", storageBucket: \"ranstax1337.appspot.com\", messagingSenderId: \"858263487640\", appId: \"1:858263487640:web:a9ee2a88f0d8c76cff7ced\"})"
)

fun initializeFirebaseForRanstax() = Firebase.initializeApp(createRanstaxFirebaseConfig()).also {
    devPrintln("Firebase app name: ${it.name}")
    val options = it.options.run {
        mapOf(
            "applicationId" to applicationId,
            "apiKey" to apiKey,
            "databaseUrl" to databaseUrl,
            "gaTrackingId" to gaTrackingId,
            "storageBucket" to storageBucket,
            "projectId" to projectId,
            "gcmSenderId" to gcmSenderId,
            "authDomain" to authDomain,
        )
    }
    devPrintln("Firebase app options: $options")
}
