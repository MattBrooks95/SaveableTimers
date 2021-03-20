package brooks.SaveableTimers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class SaveableTimer(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int?,
    @ColumnInfo(name = "duration") val duration: Int?
)