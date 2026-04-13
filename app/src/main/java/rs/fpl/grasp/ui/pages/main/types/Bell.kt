package rs.fpl.grasp.ui.pages.main.types

import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class Bell {
    val rawStrings: List<String>
    val durations: List<Duration?>

    constructor(
        rawStrings: List<String>
    ) {
        this.rawStrings = rawStrings
        durations = rawStrings.map {
            it.takeUnless { it.isBlank() }?.let { string -> Duration.parse(string) }
        }
    }


    class Duration {
        val startHour: Int
        val startMinute: Int
        val hours: Int
        val minutes: Int

        val endHour: Int
        val endMinute: Int

        constructor(startHour: Int, startMinute: Int, hours: Int, minutes: Int) {
            this.startHour = startHour
            this.startMinute = startMinute
            this.hours = hours
            this.minutes = minutes
            endHour = startHour + hours
            endMinute = startMinute + minutes
        }

        val startsIn: kotlin.time.Duration
            get() {
                val timeZone = TimeZone.currentSystemDefault()
                val now = Clock.System.now()
                val targetTime = now.toLocalDateTime(timeZone).date.atTime(startHour, startMinute)
                    .toInstant(timeZone)
                return targetTime - now
            }
        val endsIn: kotlin.time.Duration
            get() {
                val timeZone = TimeZone.currentSystemDefault()
                val now = Clock.System.now()
                val targetTime = now.toLocalDateTime(timeZone).date.atTime(endHour, endMinute)
                    .toInstant(timeZone)
                return targetTime - now
            }

        companion object {
            /**
             * @param string The string to be parsed. Format: "hh:mm-hh:mm"
             * Example usage:
             * ```
             * parse("7:00-7:45")
             * ```
             */
            fun parse(string: String): Duration = string.split('-').let { times ->
                if (times.size != 2) throw Exception("Invalid number of segments, expected 2, actual ${times.size}")
                val (startHour, startMinute) = times[0].split(':').map { it.toInt() }
                    .let { hoursMinutes ->
                        hoursMinutes[0] to hoursMinutes[1]
                    }
                val (hours, minutes) = times[1].split(':').map { it.toInt() }.let { hoursMinutes ->
                    (hoursMinutes[0] - startHour) to (hoursMinutes[1] - startMinute)
                }
                Duration(startHour, startMinute, hours, minutes)
            }
        }

        override fun toString(): String = listOf(startHour, startMinute, endHour, endMinute).map {
            it.toString().padStart(2, '0')
        }.let {
            "${it[0]}:${it[1]}-${it[2]}:${it[3]}"
        }
    }
}
