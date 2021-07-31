package brooks.SaveableTimers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ActiveTimerDao {
    @Query("SELECT * FROM SaveableTimer st INNER JOIN ActiveTimer at WHERE at.currently_active=1 AND at.uid=st.uid")
    suspend fun getAllActiveSaveableTimers(): List<SaveableTimer>
    //@Query("SELECT * FROM ActiveTimer at WHERE at.currently_active=1 AND at.uid= :uid")
    //suspend fun getActiveActiveTimerEntriesWithUid(uid: Int): List<ActiveTimer>
    @Query("SELECT * from ActiveTimer WHERE currently_active=1")
    suspend fun getAllActiveTimerEntries(): List<ActiveTimer>
    @Insert
    suspend fun insertAll(vararg ActiveTimers: ActiveTimer)
    @Query("SELECT * FROM ActiveTimer at WHERE at.uid=:uid")
    suspend fun getActiveActiveTimerEntriesForSavedTimerId(uid: Int): List<ActiveTimer>
    @Update
    suspend fun updateActiveTimerEntries(activeTimers: List<ActiveTimer>)
}
