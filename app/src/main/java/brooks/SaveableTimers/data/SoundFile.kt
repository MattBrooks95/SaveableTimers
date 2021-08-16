package brooks.SaveableTimers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys=[
    ForeignKey(
        entity=SaveableTimer::class,
        parentColumns=arrayOf("uid"),
        childColumns=arrayOf("uid"),
        onDelete=ForeignKey.NO_ACTION//deleting the one in a many-to-one relation is probably bad, right?
    )
])
data class SoundFile(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    //the selected audio file is copied into the app directory so we don't need to worry about it moving
    //unless they uninstall the app of course
    @ColumnInfo(name="uri_path") val uriPath: String
)