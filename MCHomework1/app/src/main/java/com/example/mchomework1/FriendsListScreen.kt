package com.example.mchomework1

import android.graphics.Paint.FontMetrics
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun FriendsListScreen(
    onNavigateToChat: () -> Unit,
    onPickImage: () -> Unit,
    userName: String,
    image: Uri,
    changeUserName: (String) -> Unit
    ) {
    Row (modifier = Modifier.padding(all = 8.dp)) {
        Column {
            Button(onClick = onNavigateToChat) {
                Text(text = "See chat room")
            }
            Row {
                if(image != Uri.EMPTY) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.kissa),
                        contentDescription = "Kissa kuva",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onPickImage() }) {
                    Text(text = "Select image")
                }
            }

            TextField(
                value = userName,
                onValueChange = { changeUserName(it) },
                label = { Text("Username") }
            )
        }
    }
}