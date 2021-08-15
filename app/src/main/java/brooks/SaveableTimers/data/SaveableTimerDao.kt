package brooks.SaveableTimers.data

import androidx.room.*

@Dao
interface SaveableTimerDao {
    @Query("SELECT * FROM SaveableTimer")
    suspend fun getAll(): List<SaveableTimer>

    @Query("SELECT st.*, sum(at.currently_active) FROM SaveableTimer st JOIN ActiveTimer at ON st.uid=at.uid")
    suspend fun getAllAndActiveStatus(): List<SaveableTimerWithNumberOfActiveActiveEntries>

    @Query("SELECT * from SaveableTimer WHERE uid= :uid")
    suspend fun getSaveableTimerById(uid: Int): SaveableTimer

    @Insert
    suspend fun insertAll(vararg SaveableTimers: SaveableTimer)

    @Delete
    suspend fun deleteAll(vararg SaveableTimers: SaveableTimer)
}

data class SaveableTimerWithNumberOfActiveActiveEntries(
    @ColumnInfo() val saveableTimer: SaveableTimer,
    val numberOfActiveActiveEntries: Int)