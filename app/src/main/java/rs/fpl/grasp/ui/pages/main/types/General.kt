package rs.fpl.grasp.ui.pages.main.types

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonNames
import java.util.Dictionary
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalSerializationApi::class)
@Serializable
class General constructor(
    val version: Int,
    @JsonNames("school") val schoolName: String,
    val year: String,
    val date: String,
    @JsonNames("day_count") val dayCount: Int,
    @JsonNames("per_count") val classesPerDay: Int,
    @JsonNames("rbr") private val _rbr: String,
    @JsonNames("shift") val title: String,
    @JsonNames("bells") private val _bells: List<List<String>>,
    @JsonNames("loc") val locale: Map<String, String>,
    @JsonNames("clRoman") val displayClassIndexAsRomanNumerals: Boolean,
    @JsonNames("clIndex") val classIndexVisible: Boolean,
) {
    val classNames: List<String> = _rbr.split(',')

    @Transient
    val bells: List<Bell> = _bells.map { Bell(it) }

    private fun getCurrentClassBellIndex(now: LocalDateTime): Int {
        return bells.indexOfFirst { bell ->
            bell.durations.filterNotNull().any { it.contains(now) }
        }
    }

    fun getCurrentClassBell(
        now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): Bell? {
        val index = getCurrentClassBellIndex(now)
        if(index == -1) return null
        return bells[index]
    }

    fun getNextClassBell(
        now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): Bell? {
        val index = getCurrentClassBellIndex(now)
        if(index == -1 || (index + 1) > bells.lastIndex) return null
        return bells[index + 1]
    }
}