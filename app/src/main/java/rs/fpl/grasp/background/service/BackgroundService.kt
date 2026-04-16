package rs.fpl.grasp.background.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import rs.fpl.grasp.R
import rs.fpl.grasp.background.database.types.TimetableEntity
import kotlin.time.Clock

class BackgroundService : Service() {
    lateinit var notificationManager: NotificationManager
    var timetableEntity: TimetableEntity? = null
    val scope = CoroutineScope(Dispatchers.Main)
    lateinit var timetableChangeFlow: Job

    override fun onCreate() {
        ServiceManager.onStopRequested { stopSelf() }
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)
        timetableChangeFlow = scope.launch {
            ServiceManager.timetableFlow.collect { newTimetable ->
                timetableEntity = newTimetable
                notificationManager.notify(1, createNotification())
            }
        }
        val notification = NotificationCompat.Builder(this, "service_notification")
            .setSmallIcon(R.drawable.edit_24px)
            .setContentTitle("Background service")
            .setContentText("service thingy")
            .build()
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(1000)
                notificationManager.notify(1, createNotification())
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timetableChangeFlow.cancel()
    }

    fun createNotification(): Notification {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        timetableEntity?.let { entity ->
            val currentBell = entity.timetable.general.getCurrentClassBell(now)
            val nextBell = entity.timetable.general.getNextClassBell(now)
            return Notification.Builder(this, "service_notification")
                .setSmallIcon(R.drawable.edit_24px)
                .setContentTitle("Service thing")
                .setContentText(
                    "curr: ${
                        currentBell?.durations?.filterNotNull()?.first()
                    }, next: ${nextBell?.durations?.filterNotNull()?.first()}"
                )
                .build()
        }
        return Notification.Builder(this, "service_notification")
            .setSmallIcon(R.drawable.edit_24px)
            .setContentTitle("No timetable set.")
            .build()
    }
}