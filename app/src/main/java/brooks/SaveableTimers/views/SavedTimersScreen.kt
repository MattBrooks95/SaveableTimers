package brooks.SaveableTimers.views

import android.app.AlarmManager
import android.app.PendingIntent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.Operations.TimerOperations
import brooks.SaveableTimers.R
import brooks.SaveableTimers.androidWrappers.AlarmWrapper
import brooks.SaveableTimers.androidWrappers.SavedTimerReceiver
import brooks.SaveableTimers.components.SavedTimerPanel
import brooks.SaveableTimers.data.*

import brooks.SaveableTimers.databinding.ActivitySavedTimersScreenBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: ActivitySavedTimersScreenBinding


class SavedTimersScreen : AppCompatActivity() {
    lateinit var db: AppDatabase
    private val className: String = "SavedTimersScreen"
    private var timerViewMap: MutableMap<Int, SavedTimerPanel> = mutableMapOf()
    private var timerDataMap: MutableMap<Int, SaveableTimer> = mutableMapOf()
    private val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setHandlers()
    }

    override fun onStart() {
        super.onStart()
        db = AppDatabase.getInstance(this)

        scope.launch{
            timerDataMap.clear()

            val timers: List<SaveableTimerWithNumberOfActiveActiveTimerEntries> = loadTimers()
            Log.d(className,"within the loop to make timer elements, length:" + timers.size)

            supportFragmentManager.commit{
                timerViewMap.forEach {
                    remove(it.value)
                }
                timerViewMap.clear()

                timers.forEachIndexed { _, timerAndActive ->
                    timerDataMap[timerAndActive.saveableTimer.uid] = timerAndActive.saveableTimer

                    val bundle = Bundle()
                    val timer = timerAndActive.saveableTimer
                    bundle.putString("name", timer.displayName)
                    bundle.putString("description", timer.description)
                    bundle.putInt("id", timer.uid)
                    bundle.putInt("duration", timer.duration)
                    Log.d(className, "added timer with id:${timer.uid}")
                    setReorderingAllowed(true)
                    //why can't I do this like the tutorial? in the tutorial the add method lets you pass the bundle
                    val newFragment = SavedTimerPanel()
                    timerViewMap[timer.uid] = newFragment
                    newFragment.arguments = bundle
                    newFragment.setDeleteButtonCallbackProperty(::deleteSavedTimer)
                    newFragment.setEditButtonCallbackProperty(::editSavedTimer)
                    newFragment.setActivateButtonCallback(::activateTimer)
                    newFragment.setDeactivateButtonCallback(::deactivateTimer)
                    newFragment.initialActiveValue = timerAndActive.numberOfActiveActiveTimerEntries > 0
                    add(R.id.saved_timers_container, newFragment)
                }
            }
        }
    }

    //TODO boot up create screen, pre-pop the current data and then allow them to re-save it
    private fun editSavedTimer(uuid: Int) {
        Log.d(className, "edit uuid:$uuid")
    }

    //TODO some sort of confirmation?
    private fun deleteSavedTimer(uuid: Int) {
        Log.d(className, "delete uuid:$uuid")
        val savedTimerPanel = timerViewMap[uuid];
        if (savedTimerPanel !== null) {
            supportFragmentManager.commit {
                remove(timerViewMap[uuid] as Fragment)
            }
        }
    }

    private fun activateTimer(savedTimerId: Int) {
        Log.d(className, "activate timer:$savedTimerId")
        val savedTimerData = timerDataMap[savedTimerId]
        if (savedTimerData !== null) {
            Log.d(className, "activate timer with id $savedTimerId")
            val duration = savedTimerData.duration
            val alarmManager = AlarmWrapper.getInstance(this)

            val intent = IntentFactory().createActiveTimerIntent(this, SavedTimerReceiver::class, savedTimerId)

            val pendingIntent = intent.let { intent ->
                PendingIntent.getBroadcast(this, savedTimerId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            //mins * 60 = seconds, seconds * 1000 = duration in millis
/*
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + duration * 60000,//60 secs/min * 1000 msecs/sec
                    pendingIntent
            )
*/
            alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + duration.toLong() * 60000,
                    pendingIntent
            )

            val activeTimer = ActiveTimer(0, savedTimerId, true)
            scope.launch {
                db.activeTimerDao().insertAll(activeTimer)
            }
        }
    }

    private fun deactivateTimer(uid: Int) {
        Log.d(className, "deactivate timer:$uid")
        TimerOperations().killIntentAndActiveTimerEntries(this, db, uid);
    }

    private fun setHandlers() {
        val goToActiveTimersScreenButton = binding.navigateActiveTimersButton
        goToActiveTimersScreenButton.setOnClickListener {
            val goToActiveTimersScreenIntent = IntentFactory().createGoToActiveTimersScreenIntent(this)
            startActivity(goToActiveTimersScreenIntent)
        }
    }

    private suspend fun loadTimers(): List<SaveableTimerWithNumberOfActiveActiveTimerEntries> {
        return db.saveableTimerDao().getAllAndActiveStatus()
    }

    companion object{
        const val RINGER_INTENT_TIMER_ID = "saved_timer_id"
    }
}