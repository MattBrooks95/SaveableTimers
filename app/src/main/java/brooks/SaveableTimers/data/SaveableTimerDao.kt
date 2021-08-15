package brooks.SaveableTimers.data

import androidx.room.*

@Dao
interface SaveableTimerDao {
    @Query("SELECT * FROM SaveableTimer")
    suspend fun getAll(): List<SaveableTimer>

    //every saveable timer gets returned even if it doesn't have activation records in the ActiveTimers table
    //those that do have records, will be the sum of those records that are still active, or 0
    //if they have no entries in the ActiveTimers table at all, then the null is cast to 0
    @Query("SELECT st.*, ifnull(subq.numActive, 0) sumActive FROM SaveableTimer st LEFT JOIN (SELECT at.uid, sum(at.currently_active) numActive FROM ActiveTimer at GROUP BY at.uid) subq  ON subq.uid=st.uid GROUP BY st.uid")
    suspend fun getAllAndActiveStatus(): List<SaveableTimerWithNumberOfActiveActiveTimerEntries>

    @Query("SELECT * from SaveableTimer WHERE uid= :uid")
    suspend fun getSaveableTimerById(uid: Int): SaveableTimer

    @Insert
    suspend fun insertAll(vararg SaveableTimers: SaveableTimer)

    @Delete
    suspend fun deleteAll(vararg SaveableTimers: SaveableTimer)
}