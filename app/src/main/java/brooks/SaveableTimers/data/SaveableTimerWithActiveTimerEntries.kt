package brooks.SaveableTimers.data

import androidx.room.Embedded
import androidx.room.Relation

class SaveableTimerWithActiveTimerEntries (
    @Embedded val saveableTimer: SaveableTimer,
    @Relation(
        parentColumn="uid",
        entityColumn="uid"
    )
    val activeTimers: List<ActiveTimer>
)