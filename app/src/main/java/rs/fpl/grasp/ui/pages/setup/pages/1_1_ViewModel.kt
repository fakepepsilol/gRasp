package rs.fpl.grasp.ui.pages.setup.pages

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import rs.fpl.grasp.background.TimetableManager
import rs.fpl.grasp.background.UrlProcessor
import rs.fpl.grasp.background.database.types.TimetableEntity
import javax.inject.Inject

@HiltViewModel
class SetupPageViewModel @Inject constructor() : ViewModel() {
    var timetableEntity: TimetableEntity? = null
    suspend fun fetchInfo(url: String) {
        val timetableEntity = TimetableEntity.build(
            UrlProcessor.removeNonIdParameters(url),
            UrlProcessor.fetchJson(url)
        )
        this.timetableEntity = timetableEntity
    }
    fun saveTimetable() {
        timetableEntity?.let {
            TimetableManager.saveTimetable(it)
        } ?: throw IllegalStateException("attempted to save timetable which is null")
    }
}