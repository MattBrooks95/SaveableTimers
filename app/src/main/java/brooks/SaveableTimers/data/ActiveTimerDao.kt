package brooks.SaveableTimers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ActiveTimerDao {
    @Query("SELECT * from SaveableTimer st INNER JOIN ActiveTimer at WHERE at.currently_active=1 AND at.uid=st.uid")
    suspend fun getAllActiveTimers(): List<SaveableTimer>;
    @Query("SELECT * FROM ActiveTimer at WHERE at.currently_active=1 AND at.uid= :uid")
    suspend fun getActiveActiveTimerEntriesWithUid(uid: Int): List<ActiveTimer>
    @Query("SELECT * from ActiveTimer WHERE currently_active=1")
    suspend fun getAllActiveTimerEntries(): List<ActiveTimer>;
    @Insert
    suspend fun insertAll(vararg ActiveTimers: ActiveTimer)

}
