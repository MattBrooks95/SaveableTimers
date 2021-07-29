package brooks.SaveableTimers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class ActiveTimer (
    @PrimaryKey(autoGenerate = true) val activeTimerRecordId: Int,
    @ColumnInfo(name="uid") val uid: Int,
    @ColumnInfo(name="currently_active") val currentlyActive: Boolean
)