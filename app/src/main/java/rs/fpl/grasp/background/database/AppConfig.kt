package rs.fpl.grasp.background.database

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppConfig(
    isSetupCompleted: Boolean,
    primaryTimetableId: String
) {
    var isSetupCompleted by mutableStateOf(isSetupCompleted)
    var primaryTimetableId by mutableStateOf(primaryTimetableId)
}