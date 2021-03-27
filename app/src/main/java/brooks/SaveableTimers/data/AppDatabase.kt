package brooks.SaveableTimers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SaveableTimer::class], version=4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saveableTimerDao(): SaveableTimerDao

    //singleton implementation reference https://github.com/android/sunflower/blob/main/app/src/main/java/com/google/samples/apps/sunflower/data/AppDatabase.kt
    companion object {
        @Volatile private var instance: AppDatabase? = null;

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "database-name"
            ).fallbackToDestructiveMigration().build()
        }

    }
}