package rs.fpl.grasp.background.database.types

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import rs.fpl.grasp.background.UrlProcessor
import rs.fpl.grasp.ui.pages.main.types.Timetable
import kotlin.time.Clock
import kotlin.time.Instant

@Entity(tableName = "timetables")
data class TimetableEntity (
    @PrimaryKey @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "timetableJson") val json: String,
    @ColumnInfo(name = "nickname") val dbNickname: String? = null,
    @ColumnInfo(name = "lastChange") val dbLastChange: Instant,
    @ColumnInfo(name = "lastCheck") val dbLastCheck: Instant,
) {
    val timetable: Timetable by lazy {
        Json.decodeFromString(json)
    }
    val id: String by lazy {
        UrlProcessor.getId(url)
    }
    var nickname: String? by mutableStateOf(dbNickname)
    var lastChange: Instant by mutableStateOf(dbLastChange)
    var lastCheck: Instant by mutableStateOf(dbLastCheck)

    companion object {
        fun build(url: String, json: String, nickname: String? = null): TimetableEntity {
            val now = Clock.System.now()
            return TimetableEntity(url, json, nickname, now, now)
        }
        val mutableStateSaver = Saver<MutableState<TimetableEntity?>, Map<String, Any?>>(
            save = {
                it.value?.let {
                    mapOf(
                        "url" to it.url,
                        "json" to it.json,
                        "nickname" to it.nickname,
                        "lastChange" to it.lastChange.toEpochMilliseconds(),
                        "lastCheck" to it.lastCheck.toEpochMilliseconds()
                    )
                } ?: mapOf()
            },
            restore = {
                if(it.isEmpty()) {
                    mutableStateOf(null)
                } else {
                    mutableStateOf(
                        TimetableEntity(
                            url = it["url"] as String,
                            json = it["json"] as String,
                            dbNickname = it["nickname"] as String?,
                            dbLastChange = Instant.fromEpochMilliseconds(it["lastChange"] as Long),
                            dbLastCheck = Instant.fromEpochMilliseconds(it["lastCheck"] as Long)
                        )
                    )
                }
            }
        )
    }
}

