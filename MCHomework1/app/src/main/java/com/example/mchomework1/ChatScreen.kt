package com.example.mchomework1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ChatScreen(
    onNavigateToFriends: () -> Unit
) {
    Column {
        Button(onClick = onNavigateToFriends) {
            Text(text = "See friends list")
        }
        Conversation(SampleData.conversationSample)
    }
}