package brooks.SaveableTimers.androidWrappers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import brooks.SaveableTimers.views.RingingScreen


class SavedTimerReceiver : BroadcastReceiver() {
    private val className: String = "SavedTimerReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(className, "the alarm was fired!")
        val launchViewIntent: Intent = Intent(context, RingingScreen::class.java)
        launchViewIntent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivity(context, launchViewIntent, null)
    }
}
