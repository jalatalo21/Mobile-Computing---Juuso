package com.example.mchomework1

import androidx.compose.runtime.Composable

/**
 * com.example.mchomework1.SampleData for Jetpack Compose Tutorial
 */

object SampleData {

    val messageSample = Message(
        "Kissa",
        "Test...Test...Test..."
    )
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            "Kissa",
            "Test...Test...Test..."
        ),
        Message(
            "Kissa",
            """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim()
        ),
        Message(
            "Kissa",
            """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim()
        ),
        Message(
            "Kissa",
            "Searching for alternatives to XML layouts..."
        ),
        Message(
            "Kissa",
            """Hey, take a look at Jetpack Compose, it's great!
            |It's the Android's modern toolkit for building native UI.
            |It simplifies and accelerates UI development on Android.
            |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim()
        ),
        Message(
            "Kissa",
            "It's available from API 21+ :)"
        ),
        Message(
            "Kissa",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            "Kissa",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            "Kissa",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            "Kissa",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            "Kissa",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            "Kissa",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            "Kissa",
            "Have you tried writing build.gradle with KTS?"
        ),
    )
}
