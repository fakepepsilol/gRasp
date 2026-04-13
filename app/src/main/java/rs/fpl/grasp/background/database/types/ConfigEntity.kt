package rs.fpl.grasp.background.database.types

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class ConfigEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "setupCompleted", defaultValue = "false") val setupCompleted: Boolean = false,
    @ColumnInfo(name = "primaryTimetableId") val primaryTimetableId: String? = null
)