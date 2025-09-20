package com.fakepepsilol.grasp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fakepepsilol.grasp.data.Config
import com.fakepepsilol.grasp.ui.Main
import com.fakepepsilol.grasp.ui.theme.GRaspTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GRaspTheme {
                Main()
            }
        }
    }

}

@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var config: Config

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch()
        {
            config.preload()
        }
    }
}