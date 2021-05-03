package brooks.SaveableTimers.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaveableTimerDao {
    @Query("SELECT * FROM saveabletimer")
    suspend fun getAll(): List<SaveableTimer>

    @Query("SELECT * from saveableTimer WHERE uid= :uid")
    suspend fun getSaveableTimerById(uid: Int): SaveableTimer

    @Insert
    suspend fun insertAll(vararg saveableTimers: SaveableTimer)

    @Delete
    suspend fun deleteAll(vararg saveableTimers: SaveableTimer)
}