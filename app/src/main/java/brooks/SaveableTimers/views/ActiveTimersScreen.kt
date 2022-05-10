package brooks.SaveableTimers.views

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.Operations.TimerOperations
import brooks.SaveableTimers.R
import brooks.SaveableTimers.brooks.SaveableTimers.views.SaveableTimersBaseActivity
import brooks.SaveableTimers.components.ActiveTimerPanel
import brooks.SaveableTimers.components.SavedTimerPanel
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.ActiveTimersScreenBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: ActiveTimersScreenBinding

class ActiveTimersScreen : SaveableTimersBaseActivity() {
    private lateinit var db: AppDatabase
    private val className: String = "ActiveTimersScreen"
    private var timerViewMap: MutableMap<Int, SavedTimerPanel> = mutableMapOf()
    private val scope = MainScope()
    private lateinit var debugView: TextView
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
        debugView = TextView(applicationContext)
        binding.root.addView(debugView)
    }

    override fun onStart() {
        super.onStart()
        scope.launch {
            val timers: List<SaveableTimer> = db.activeTimerDao().getAllActiveSaveableTimers()
            supportFragmentManager.commit {
                timerViewMap.forEach {
                    remove(it.value)
                }
                timerViewMap.clear()
                timers.forEach { timer ->
                //timers.forEachIndexed { index, timer ->
                    val bundle = Bundle()
                    //bundle.putString("name", timer.displayName)
                    //bundle.putString("description", timer.description)
                    bundle.putInt("id", timer.uid)
                    bundle.putString("name", timer.displayName)
                    bundle.putString("description", timer.description)
                    //TODO showing the duration of the timer is pointless, need to show a countdown
                    bundle.putInt("duration", timer.duration);
                    //Log.d(className, "added active timer panel with id:${timer.uid}")
                    setReorderingAllowed(true)
                    //why can't I do this like the tutorial? in the tutorial the add method lets you pass the bundle
                    //val newFragment = ActiveTimerPanel()
                    bundle.putBoolean("is_active_panel", true)
                    val newFragment = SavedTimerPanel()
                    newFragment.arguments = bundle
                    newFragment.setDeactivateButtonCallback(::deactivateTimer)
                    add(R.id.active_timers_container, newFragment)
                    timerViewMap[timer.uid] = newFragment
                }
            }
        }
    }

    private fun deactivateTimer(savedTimerId: Int) {
        Log.d(className, "deactivate timer with ID:$savedTimerId")
        TimerOperations().killIntentAndActiveTimerEntries(this, db, savedTimerId)
        val savedTimerView = timerViewMap[savedTimerId]
        supportFragmentManager.commit {
            //supportFragmentManager.commit {
                remove(savedTimerView as Fragment)
            //}
        }
    }

    private fun setHandlers() {
        //Log.d(className, "setHandlers")
        val toCreateTimerScreenButton = binding.navigateCreateTimerButton
        toCreateTimerScreenButton.setOnClickListener {
            //Log.d(className, "Go to make timer screen")
            val goToCreateTimerIntent = IntentFactory().makeGoToCreateTimerScreenIntent(this)
            startActivity(goToCreateTimerIntent)
        }

        val toSavedTimersScreenButton = binding.navigateSavedTimersButton
        toSavedTimersScreenButton.setOnClickListener {
           // Log.d(className, "go to saved timers screen")
            val goToSavedTimersScreenIntent = IntentFactory().makeGoToSavedTimersScreenIntent(this)
            startActivity(goToSavedTimersScreenIntent);
        }
    }
}