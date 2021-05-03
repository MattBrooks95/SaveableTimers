package brooks.SaveableTimers.views

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import brooks.SaveableTimers.R
import brooks.SaveableTimers.androidWrappers.AlarmWrapper
import brooks.SaveableTimers.androidWrappers.SavedTimerReceiver
import brooks.SaveableTimers.components.SavedTimerPanel
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer

import brooks.SaveableTimers.databinding.ActivitySavedTimersScreenBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: ActivitySavedTimersScreenBinding


class SavedTimersScreen : AppCompatActivity() {
    lateinit var db: AppDatabase
    private val className: String = "SavedTimersScreen"
    private var timerViewMap: MutableMap<Int, SavedTimerPanel> = mutableMapOf()
    private var timerDataMap: MutableMap<Int, SaveableTimer> = mutableMapOf()
    val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setHandlers()

        db = AppDatabase.getInstance(this)
        setHandlers()

        val appContext = this

        scope.launch{
            val timers: List<SaveableTimer> = loadTimers()
            Log.d(className,"within the loop to make timer elements, length:" + timers.size)
            timers.forEach {
                timerDataMap[it.uid] = it
            }

            supportFragmentManager.commit{
                timers.forEachIndexed { index, timer ->
                    val bundle = Bundle()
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
                    add(R.id.saved_timers_container, newFragment)
                }
            }
//            }
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
//            scope.launch {
//                val timerDao = db.saveableTimerDao()
//                timerDao.deleteAll(savedTimerPanel.savedTimerData);
//                binding.savedTimersContainer.removeView(savedTimerPanel)
//            }
        }
    }

    private fun activateTimer(uuid: Int) {
        Log.d(className, "activate timer:$uuid")
        val savedTimerData = timerDataMap[uuid]
        if (savedTimerData !== null) {
            Log.d(className, "activate timer with id $uuid")
            val duration = savedTimerData.duration
            val alarmManager = AlarmWrapper.getInstance(this)

            val intent = Intent(this, SavedTimerReceiver::class.java)
            intent.putExtra(RINGER_INTENT_TIMER_ID, uuid)

            val pendingIntent = intent.let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            //mins * 60 = seconds, seconds * 1000 = duration in millis
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + duration * 60 * 1000,
                    pendingIntent
            )
        }
    }

    private fun deactivateTimer(uuid: Int) {
        Log.d(className, "deactivate timer:$uuid")
        val savedTimerPanel = timerViewMap[uuid]
        if (savedTimerPanel !== null) {
            Log.d(className, "deactivate timer with id $uuid")
        }
    }

//    private fun buildViewForSavedTimer(savedTimer: SaveableTimer): SavedTimerPanel {
//        val savedTimerPanel = SavedTimerPanel(this, savedTimer)
        //this double colon syntax was necessary to pass the method as a parameter
//        savedTimerPanel.setEditButtonCallback(::editSavedTimer)
//        savedTimerPanel.setDeleteButtonCallback(::deleteSavedTimer)
//        savedTimerPanel.setActivateButtonCallback(::activateTimer)
//        savedTimerPanel.setDeactivateButtonCallback(::deactivateTimer)
//        return savedTimerPanel
//    }

    private fun setHandlers() {
        val goToActiveTimersScreenButton = binding.navigateActiveTimersButton
        goToActiveTimersScreenButton.setOnClickListener {
            val goToActiveTimersScreenIntent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(goToActiveTimersScreenIntent)
        }
    }

    private suspend fun loadTimers(): List<SaveableTimer> {
        return db.saveableTimerDao().getAll()
    }

    companion object{
        const val RINGER_INTENT_TIMER_ID = "saved_timer_id"
    }
}