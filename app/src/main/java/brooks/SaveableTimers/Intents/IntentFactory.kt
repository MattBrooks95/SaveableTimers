package brooks.SaveableTimers.Intents

import android.content.Context
import android.content.Intent
import brooks.SaveableTimers.androidWrappers.SavedTimerReceiver
import brooks.SaveableTimers.views.SavedTimersScreen
import kotlin.reflect.KClass

class IntentFactory {
    fun createActiveTimerIntent(appContext: Context, receiverClass: KClass<*>, timerId: Int): Intent {
        val intent = Intent(appContext, receiverClass.java)
        intent.putExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, timerId)
        return intent
    }
}