package com.example.mchomework1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FriendsListScreen(
    onNavigateToChat: () -> Unit
) {
    Column {
        Button(onClick = onNavigateToChat) {
            Text(text = "See chat room")
        }
        Text(text = "It seems you have no friends :(")
    }
}