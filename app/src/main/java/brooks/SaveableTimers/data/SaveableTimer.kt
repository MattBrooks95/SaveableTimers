package brooks.SaveableTimers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class SaveableTimer(
    @PrimaryKey(autoGenerate = true) val uid: Int,
//    @ColumnInfo(name = "tag_id") val tagId: Int?,//key into tags table, for search feature
    @ColumnInfo(name = "display_name") val displayName: String?,
    @ColumnInfo(name = "duration") val duration: Int?
)