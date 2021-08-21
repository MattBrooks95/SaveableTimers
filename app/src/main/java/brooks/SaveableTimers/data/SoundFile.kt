package brooks.SaveableTimers.data

import androidx.room.*

@Entity()
data class SoundFile(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    //the selected audio file is copied into the app directory so we don't need to worry about it moving
    //unless they uninstall the app of course
    @ColumnInfo(name="uri_path") val uriPath: String
)

data class SoundFileWithTimers(
    @Embedded val soundFile: SoundFile,
    @Relation(
        parentColumn="uid",
        entityColumn="sound_file_id"
    )
    val timers: List<SaveableTimer>
)