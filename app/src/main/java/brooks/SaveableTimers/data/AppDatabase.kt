package brooks.SaveableTimers.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SaveableTimer::class], version=1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saveableTimerDao(): SaveableTimerDao
}