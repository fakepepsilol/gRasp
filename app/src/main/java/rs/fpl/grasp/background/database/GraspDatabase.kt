package rs.fpl.grasp.background.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import rs.fpl.grasp.background.database.types.ConfigDao
import rs.fpl.grasp.background.database.types.ConfigEntity
import rs.fpl.grasp.background.database.types.TimetableDao
import rs.fpl.grasp.background.database.types.TimetableEntity
import rs.fpl.grasp.background.database.types.converters.InstantConverter
import rs.fpl.grasp.ui.pages.main.types.Timetable
import javax.inject.Inject

@Database(entities = [TimetableEntity::class, ConfigEntity::class], version = 1)
@TypeConverters(InstantConverter::class)
abstract class GraspDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
    abstract fun configDao(): ConfigDao
    companion object {
        private var INSTANCE: GraspDatabase? = null
        fun getInstance(): GraspDatabase? = INSTANCE
        fun getInstance(
            context: Context
        ): GraspDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    GraspDatabase::class.java,
                    "database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}