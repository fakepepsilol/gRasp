package rs.fpl.grasp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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