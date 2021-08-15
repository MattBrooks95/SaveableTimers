package brooks.SaveableTimers.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

class SaveableTimerWithNumberOfActiveActiveTimerEntries (
    @Embedded val saveableTimer: SaveableTimer,
    @ColumnInfo(name="sumActive") val numberOfActiveActiveTimerEntries: Int
)