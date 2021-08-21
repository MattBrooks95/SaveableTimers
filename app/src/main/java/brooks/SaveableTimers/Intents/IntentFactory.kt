package brooks.SaveableTimers.Intents

import android.content.Context
import android.content.Intent
import brooks.SaveableTimers.androidWrappers.SavedTimerReceiver
import brooks.SaveableTimers.views.ActiveTimersScreen
import brooks.SaveableTimers.views.CreateTimerScreen
import brooks.SaveableTimers.views.RingingScreen
import brooks.SaveableTimers.views.SavedTimersScreen
import kotlin.reflect.KClass

class IntentFactory {
    fun createActiveTimerIntent(appContext: Context, receiverClass: KClass<*>, timerId: Int): Intent {
        val intent = Intent(appContext, receiverClass.java)
        intent.putExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, timerId)
        return intent
    }

    //TODO is it possible to specify the class here?
    fun createTimerWasDismissedIntent(appContext: Context, timerId: Int): Intent {
        val alarmDismissedIntent = Intent()
        alarmDismissedIntent.action = SavedTimersScreen.TIMER_WAS_DISMISSED_INTENT
        alarmDismissedIntent.putExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, timerId)
        return alarmDismissedIntent
    }

    fun createGoToActiveTimersScreenIntent(appContext: Context): Intent {
        return Intent(appContext, ActiveTimersScreen::class.java)
    }

    fun makeGoToCreateTimerScreenIntent(appContext: Context): Intent {
        return Intent(appContext, CreateTimerScreen::class.java)
    }

    fun makeGoToRingingScreenIntent(appContext: Context): Intent {
        return Intent(appContext, RingingScreen::class.java)
    }

    fun makeGoToSavedTimersScreenIntent(appContext: Context): Intent {
        return Intent(appContext, SavedTimersScreen::class.java)
    }
}