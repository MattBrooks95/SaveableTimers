package brooks.SaveableTimers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SoundFileDao {
    @Query("SELECT * FROM SoundFile")
    suspend fun getAll(): List<SoundFile>
    @Query("SELECT * FROM SoundFile WHERE uri_path=:uriPath")
    suspend fun getSoundFileForPath(uriPath: String): SoundFile
    @Query("SELECT * FROM SoundFile sf WHERE sf.uid=:soundFileId")
    suspend fun getSoundFileForId(soundFileId: Int): SoundFile
    @Insert
    suspend fun insertAll(vararg soundFiles: SoundFile)
    @Insert
    suspend fun insert(soundFile: SoundFile): Long
}