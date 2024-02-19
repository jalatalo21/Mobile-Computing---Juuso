package com.example.mchomework1

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.mchomework1.ui.theme.MCHomework1Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var mSensor: Sensor? = null
    private var notification: Notification? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        notification = Notification(this)
        createNotificationChannel(this)

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

    public var notif = 1

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        if ((notif == 1) and (x > 2)) {
            Log.d("Debugaus", "Nostitko juuri kännykän? gyroX = $x")
            notif = 0
            notification?.showNotification()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        notif = 1
        mSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}

data class Message(val author: String, val body: String)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "Chat",
    database: AppDatabase,
) {
    val postNotificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    LaunchedEffect(key1 = true) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
    }
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

private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "MC Channel"
        val descriptionText = "Notification channel for mobile computing course"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
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