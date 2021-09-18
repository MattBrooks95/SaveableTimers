package brooks.SaveableTimers.androidWrappers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.R
import brooks.SaveableTimers.views.RingingScreen
import brooks.SaveableTimers.views.SavedTimersScreen


class SavedTimerReceiver : BroadcastReceiver() {
    private val className: String = "SavedTimerReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        val savedTimerId: Int = intent.getIntExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, -1)
        if (savedTimerId == -1) {
            Log.e(className, "Saved timer id wasn't found in intent received by $className")
        }
        Log.d(className, "the alarm was fired! intent savedTimerId:$savedTimerId")
        val launchViewIntent: Intent = IntentFactory().makeGoToRingingScreenIntent(context)
        //there has to be a smarter way to pass this through to the activity
        //TODO what should my error handling look like here, this will just pass -1 to the activity
        launchViewIntent.putExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, savedTimerId)
        launchViewIntent.flags = FLAG_ACTIVITY_NEW_TASK
        Log.d(className, launchViewIntent.toString())
        //the fact that i need to work around it like this is really stupid
        //the user has to tap a tiny notification tray in order for the ALARM app to show the big, easy to hit button to STOP THE ALARM
        //can't make it up
//        startActivity(context, launchViewIntent, null)
        val channelId = "somethingsomething"
        val pendingLaunchViewIntent = PendingIntent.getActivity(context, savedTimerId, launchViewIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //startActivity(context, launchViewIntent, null)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_android_black_24dp)//It needs to have an icon or it will error out...
//            .setContentTitle("why do I need to do this I just want to launch an activity")
            .setContentTitle("SaveableTimer")// but my activity has the text I want to show...
            .setAutoCancel(true)//cancel as soon as they tap the nofication
            //makes it so that android is more likely to show the notification regardless  of the state of the phone
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //finally launch the activity that should have just simply been launched at the beginning...
            .setFullScreenIntent(pendingLaunchViewIntent, true)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                channelId,
                "somethingelse",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        val finalNotification = notificationBuilder.build()
        notificationManager.notify(savedTimerId, finalNotification)
    }
}
