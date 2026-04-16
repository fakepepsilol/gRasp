package rs.fpl.grasp.background.database.types

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConfigDao {

    @Insert
    suspend fun insertRow(value: ConfigEntity)

    @Query("SELECT COUNT(*) FROM config")
    fun getRowCount(): Int

    suspend fun createRowIfMissing(defaultValue: ConfigEntity = ConfigEntity()) {
        if(getRowCount() == 0) {
            insertRow(defaultValue)
        }
    }

    @Query("UPDATE config SET setupCompleted = true")
    suspend fun onSetupCompleted()

    @Query("SELECT config.setupCompleted FROM config LIMIT 1")
    suspend fun isSetupCompleted(): Boolean

    @Query("SELECT config.primaryTimetableId FROM config LIMIT 1")
    suspend fun getPrimaryTimetableId(): String?

    @Query("UPDATE config SET primaryTimetableId = :id")
    suspend fun setPrimaryTimetableId(id: String?)

    @Query("DELETE FROM config")
    fun deleteConfig()
}