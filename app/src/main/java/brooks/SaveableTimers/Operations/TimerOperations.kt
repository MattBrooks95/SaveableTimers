package brooks.SaveableTimers.Operations

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.androidWrappers.AlarmWrapper
import brooks.SaveableTimers.androidWrappers.SavedTimerReceiver
import brooks.SaveableTimers.data.ActiveTimer
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.views.SavedTimersScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TimerOperations {
    val className: String = "TimerOperations"
    private val scope: CoroutineScope = MainScope()

    fun killIntentAndActiveTimerEntries(context: Context, db: AppDatabase, timerId: Int) {
        Log.d(className, "deactivate timer:$timerId")
        killTimerIntent(context, timerId)
        deactivateActiveTimerEntries(context, db, timerId)
    }

    private fun killTimerIntent(context: Context, timerId: Int) {
        Log.d(className, "kill timer intent with id $timerId")
        val intent = IntentFactory().createActiveTimerIntent(context, SavedTimerReceiver::class, timerId)

        val pendingIntent = intent.let { intent ->
            PendingIntent.getBroadcast(context, timerId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        AlarmWrapper.getInstance(context).cancel(pendingIntent)
    }

    fun deactivateActiveTimerEntries(context: Context, db: AppDatabase, timerId: Int) {
        scope.launch {
            val timersToTurnOff: List<ActiveTimer> = db.activeTimerDao().getActiveActiveTimerEntriesForSavedTimerId(timerId)
            val mutableTimersToTurnOff = timersToTurnOff.toMutableList()
            mutableTimersToTurnOff.forEach {
                it.currentlyActive = false
            }
            db.activeTimerDao().updateActiveTimerEntries(mutableTimersToTurnOff)
        }
    }
}

