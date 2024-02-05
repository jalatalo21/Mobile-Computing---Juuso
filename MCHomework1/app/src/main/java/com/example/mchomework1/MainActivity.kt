package com.example.mchomework1

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mchomework1.ui.theme.MCHomework1Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import android.content.res.Configuration
import android.net.Uri
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.MainScope
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "UserDatabase"
        ).allowMainThreadQueries().build()

        setContent {
            MCHomework1Theme {
                MyAppNavHost(database = db)
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Chat",
    database: AppDatabase
) {
    val userDao = database.userDao()
    val size: List<User> = userDao.getAll()
    if(size.isEmpty()) {
        val user = User(
            uid = 1,
            userName = "Kissa",
            image = Uri.EMPTY.toString()
        )
        userDao.insertUser(user)
    }

    val users: List<User> = userDao.getAll()
    var userName by remember {
        mutableStateOf<String>(users[0].userName)
    }
    var image by remember {
        mutableStateOf<Uri>(users[0].image.toUri())
    }

    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri -> if(uri != null) {
                val new_image = imageToStorage(context, uri)
                image = new_image
                val user = User(
                    uid = users[0].uid,
                    userName,
                    image.toString()
                )
                userDao.updateUser(user)
            }
        }
    )

    fun changeUserName(new_name: String) {
        userName = new_name
        val user = User(
            uid = users[0].uid,
            userName,
            image.toString()
        )
        userDao.updateUser(user)
    }

    fun launchPhotoPicker() {
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("Chat") {
            ChatScreen(
                onNavigateToFriends = {
                    navController.navigate("friendsList")
                },
                userName = userName,
                image = image
            )
        }
        composable("friendsList") {
            FriendsListScreen(
                onNavigateToChat = {
                    navController.navigate("Chat") {
                        popUpTo("Chat") {
                            inclusive = true
                        }
                    }
                },
                onPickImage = { launchPhotoPicker() },
                userName = userName,
                image = image,
                changeUserName = { changeUserName(it) }
            )
        }
    }
}

fun imageToStorage(context: Context, uri: Uri): Uri {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput("image.jpg", Context.MODE_PRIVATE)
    inputStream?.use {
        input -> outputStream?.use {
            output -> input.copyTo(output)
        }
    }
    val file = File(context.filesDir, "image.jpg")
    return Uri.fromFile(file)
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)

@Preview
@Composable
fun PreviewMessageCard() {
    MCHomework1Theme {
        Surface {
            MessageCard(SampleData.messageSample, userName = "Kissa", image = Uri.EMPTY)
        }
    }
}