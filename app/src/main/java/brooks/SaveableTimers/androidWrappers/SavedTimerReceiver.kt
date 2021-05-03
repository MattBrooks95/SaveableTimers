package brooks.SaveableTimers.androidWrappers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import brooks.SaveableTimers.views.RingingScreen
import brooks.SaveableTimers.views.SavedTimersScreen


class SavedTimerReceiver : BroadcastReceiver() {
    private val className: String = "SavedTimerReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(className, "the alarm was fired!")
        val savedTimerId: Int = intent.getIntExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, -1)
        if (savedTimerId === -1) {
            Log.e(className, "Saved timer id wasn't found in intent received by $className")
        }
        val launchViewIntent: Intent = Intent(context, RingingScreen::class.java)
        //there has to be a smarter way to pass this through to the activity
        //TODO what should my error handling look like here, this will just pass -1 to the activity
        launchViewIntent.putExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, savedTimerId)
        launchViewIntent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(context, launchViewIntent, null)
    }
}
