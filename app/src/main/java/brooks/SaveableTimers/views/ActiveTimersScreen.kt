package brooks.SaveableTimers.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.Operations.TimerOperations
import brooks.SaveableTimers.R
import brooks.SaveableTimers.components.ActiveTimerPanel
import brooks.SaveableTimers.components.SavedTimerPanel
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.ActiveTimersScreenBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: ActiveTimersScreenBinding

class ActiveTimersScreen: AppCompatActivity() {
    private lateinit var db: AppDatabase;
    private val className: String = "ActiveTimersScreen"
    private var timerViewMap: MutableMap<Int, ActiveTimerPanel> = mutableMapOf()
    private lateinit var broadcastReceiver: BroadcastReceiver
    val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiveTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        setHandlers()

        val newLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.25f
        )
        newLayoutParams.bottomMargin = 5
        val appContext = this

        scope.launch {
            val timers: List<SaveableTimer> = db.activeTimerDao().getAllActiveSaveableTimers()
            supportFragmentManager.commit {
                timers.forEachIndexed { index, timer ->
                    val bundle = Bundle()
                    bundle.putString("name", timer.displayName)
                    bundle.putString("description", timer.description)
                    bundle.putInt("id", timer.uid)
                    bundle.putInt("ringTime", 1776)//TODO actual ring time
                    Log.d(className, "added active timer panel with id:${timer.uid}")
                    setReorderingAllowed(true)
                    //why can't I do this like the tutorial? in the tutorial the add method lets you pass the bundle
                    val newFragment = ActiveTimerPanel()
                    newFragment.arguments = bundle
                    newFragment.setDeactivateButtonCallback(::deactivateTimer)
                    add(R.id.active_timers_container, newFragment)
                    timerViewMap.put(timer.uid, newFragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupAlarmRangMessageReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        tearDownAlarmRangMessageReceiver()
    }

    private fun setupAlarmRangMessageReceiver() {
        broadcastReceiver = UpdateReceiver()
        (broadcastReceiver as UpdateReceiver).updateActivityCallback = ::removeActiveTimerPanel
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SavedTimersScreen.TIMER_WAS_DISMISSED_INTENT)
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    //TODO on resume, on destroy, receiver's code should be able to be put in a parent class or something
    //TODO we enter the callback twice for some reason, I think some wires are crossed between the saved and active timer screens
    class UpdateReceiver : BroadcastReceiver() {
        lateinit var updateActivityCallback: (savedTimerId: Int) -> Unit?
        override fun onReceive(context: Context, intent: Intent): Unit {
            if (intent == null) {
                Log.e("ActiveTimersScreen", "no intent!")
            }
            val savedTimerId = intent.getIntExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, -1)
            if (savedTimerId == -1) {
                Log.e("ActiveTimersScreen", "intent didn't have saved timer id with key ${SavedTimersScreen.RINGER_INTENT_TIMER_ID}")
            } else if (updateActivityCallback != null) {
                updateActivityCallback(savedTimerId)
            }
        }
    }

    private fun getLocalBroadCastManager(): LocalBroadcastManager {
        return LocalBroadcastManager.getInstance(this)
    }

    private fun tearDownAlarmRangMessageReceiver() {
        val localBroadcastManager = getLocalBroadCastManager()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
    }

    private fun removeActiveTimerPanel(savedTimerId: Int) {
        val activeSavedTimerView = timerViewMap[savedTimerId]
        if (activeSavedTimerView != null) {
            supportFragmentManager.commit {
                remove(activeSavedTimerView)
            }
        }
    }

    private fun deactivateTimer(savedTimerId: Int) {
        TimerOperations().deactivateTimer(this, db, savedTimerId)
        val savedTimerView = timerViewMap.get(savedTimerId)
        supportFragmentManager.commit {
            supportFragmentManager.commit {
                remove(savedTimerView as Fragment)
            }
        }
    }

    private fun setHandlers() {
        Log.d(className, "setHandlers")
        val toCreateTimerScreenButton = binding.navigateCreateTimerButton
        toCreateTimerScreenButton.setOnClickListener {
            Log.d(className, "Go to make timer screen")
            val goToCreateTimerIntent = IntentFactory().makeGoToCreateTimerScreenIntent(this)
            startActivity(goToCreateTimerIntent)
        }

        val toSavedTimersScreenButton = binding.navigateSavedTimersButton
        toSavedTimersScreenButton.setOnClickListener {
            Log.d(className, "go to saved timers screen")
            val goToSavedTimersScreenIntent = IntentFactory().makeGoToSavedTimersScreenIntent(this)
            startActivity(goToSavedTimersScreenIntent);
        }
    }
}