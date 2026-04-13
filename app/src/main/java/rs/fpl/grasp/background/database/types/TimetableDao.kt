package rs.fpl.grasp.background.database.types

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    @Query("SELECT * FROM timetables")
    suspend fun getAllRows(): List<TimetableEntity>

    @Query("SELECT * FROM timetables")
    fun getAllRowsAsFlow(): Flow<List<TimetableEntity>>

    @Upsert
    suspend fun upsert(timetable: TimetableEntity)

    @Query("DELETE FROM timetables")
    suspend fun deleteAll()

    @Query("DELETE FROM timetables WHERE instr(url, '/?pid=' || :id) > 0")
    suspend fun deleteById(id: String)
}