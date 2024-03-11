package com.example.mchomework1

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import coil.compose.AsyncImage

@Composable
fun ChatScreen(
    onNavigateToFriends: () -> Unit,
    userName: String,
    image: Uri
) {
    Row (modifier = Modifier.padding(all = 8.dp)) {
        Column {
            Button(onClick = onNavigateToFriends) {
                Text(text = "See profile screen")
            }

            var text by remember { mutableStateOf("") }
            var viesti by remember { mutableStateOf(Message("", ""))}

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val textFieldDefaultHorPad = 16.dp
                val maxInputWidth = maxWidth - 8 * textFieldDefaultHorPad
                Row {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("New message") },
                        modifier = Modifier.width(maxInputWidth)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viesti = Message("author", text)
                        addMessage(viesti)
                        text = "" }
                    ) {
                        Text(text = "Send")
                    }
                }
            }
            Conversation(messages = SampleData.conversationSample, userName = userName, image = image)
            MessageCard(msg = viesti, userName = userName, image = image)
        }
    }
}

@Composable
fun Conversation(
    messages: List<Message>,
    userName: String,
    image: Uri
) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message, userName, image)
        }
    }
}

@Composable
fun MessageCard(
    msg: Message,
    userName: String,
    image: Uri
) {
    Row (modifier = Modifier.padding(all = 8.dp)){
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

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "",
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}