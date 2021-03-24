package brooks.SaveableTimers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaveableTimerDao {
    @Query("SELECT * FROM saveabletimer")
    suspend fun getAll(): List<SaveableTimer>

    @Insert
    fun insertAll(vararg saveableTimers: SaveableTimer)
}