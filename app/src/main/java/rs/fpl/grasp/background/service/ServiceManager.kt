package rs.fpl.grasp.background.service

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import rs.fpl.grasp.background.database.types.TimetableEntity

object ServiceManager {
    fun startService(context: Context) {
        val intent = Intent(context, BackgroundService::class.java)
        context.startForegroundService(intent)
    }

    private val stopCallbacks: MutableList<() -> Unit> = mutableListOf()
    fun stopService() {
        stopCallbacks.forEach { callback -> callback() }
        stopCallbacks.clear()
    }

    fun onStopRequested(callback: () -> Unit) {
        stopCallbacks.add(callback)
    }

    fun restartService(context: Context) {
        stopService()
        startService(context)
    }

    private val _timetableFlow: MutableSharedFlow<TimetableEntity?> = MutableSharedFlow(
        1, 8,
        BufferOverflow.DROP_OLDEST
    )
    val timetableFlow = _timetableFlow.asSharedFlow()

    fun setTimetable(entity: TimetableEntity?) {
        _timetableFlow.tryEmit(entity)
    }
}