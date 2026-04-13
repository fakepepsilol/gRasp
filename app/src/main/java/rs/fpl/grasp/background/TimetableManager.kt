package rs.fpl.grasp.background

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.fpl.grasp.background.database.GraspDatabase
import rs.fpl.grasp.background.database.types.TimetableEntity

object TimetableManager {
    val timetables: SnapshotStateList<TimetableEntity> = mutableStateListOf()
    var primaryTimetableId: String? by mutableStateOf(null)
    private val scope = CoroutineScope(Dispatchers.IO)
    suspend fun initialize() {
        GraspDatabase.getInstance()?.let { db ->
            db.timetableDao().getAllRows().let { rows ->
                timetables.addAll(rows)
            }
            primaryTimetableId =
                db.configDao().getPrimaryTimetableId() ?: timetables.firstOrNull()?.id
        }
    }

    fun deleteTimetable(id: String): Boolean {
        timetables.indexOfFirst { it.url.endsWith("?pid=$id") }.let { index ->
            if (index == -1) return false
            timetables.removeAt(index)
            scope.launch {
                GraspDatabase.getInstance()?.timetableDao()?.deleteById(id)
            }
            return true
        }
    }

    fun saveTimetable(entity: TimetableEntity) {
        val index = timetables.indexOfFirst { it.url == entity.url }
        if (index == -1) {
            timetables.add(entity)
        } else {
            timetables.removeAt(index)
            timetables.add(index, entity)
        }
        scope.launch {
            GraspDatabase.getInstance()?.let { db ->
                if(timetables.count() == 1) {
                    setPrimary(entity)
                }
                db.timetableDao().upsert(entity)
            }
        }
    }

    fun getPrimary(): TimetableEntity? =
        primaryTimetableId?.let { id ->
            timetables.first { it.id == id }
        }
    fun setPrimary(entity: TimetableEntity) {
        primaryTimetableId = entity.id
        scope.launch {
            GraspDatabase.getInstance()?.configDao()?.setPrimaryTimetableId(entity.id)
        }
    }
}
