package brooks.SaveableTimers.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SoundFileDao {
    @Query("SELECT * FROM SoundFile")
    suspend fun getAll(): List<SoundFile>
}