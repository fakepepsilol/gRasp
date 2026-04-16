package rs.fpl.grasp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ServiceCompat
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rs.fpl.grasp.background.TimetableManager
import rs.fpl.grasp.background.database.AppConfig
import rs.fpl.grasp.background.database.GraspDatabase
import rs.fpl.grasp.background.database.types.ConfigEntity
import rs.fpl.grasp.background.service.BackgroundService
import rs.fpl.grasp.background.service.ServiceManager
import rs.fpl.grasp.ui.pages.main.MainPage
import rs.fpl.grasp.ui.pages.setup.SetupPages
import rs.fpl.grasp.ui.theme.GRaspTheme

var isSetupCompleted by mutableStateOf(false)

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        GraspDatabase.getInstance(this@App).also { database ->
            CoroutineScope(Dispatchers.IO).launch {
                database.configDao().createRowIfMissing()
            }
        }.also { database ->
            runBlocking {
                CoroutineScope(Dispatchers.IO).launch {
                    isSetupCompleted = database.configDao().isSetupCompleted()
                }.join()
                TimetableManager.initialize()
            }
        }
        createNotificationChannels()
        ServiceManager.startService(this)
        TimetableManager.getPrimary()?.let { entity ->
            ServiceManager.setTimetable(entity)
        }
    }
    private fun createNotificationChannels() {
        val mChannel = NotificationChannel(
            "service_notification",
            "Service Notification",
            NotificationManager.IMPORTANCE_LOW
        )
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(mChannel)
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GRaspTheme {
                if (!isSetupCompleted) {
                    SetupPages()
                }
                MainPage()
            }
        }

    }
}