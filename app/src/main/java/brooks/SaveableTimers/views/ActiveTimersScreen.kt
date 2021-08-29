package brooks.SaveableTimers.views

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.Operations.TimerOperations
import brooks.SaveableTimers.R
import brooks.SaveableTimers.components.ActiveTimerPanel
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
    private val scope = MainScope()
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
                    timerViewMap[timer.uid] = newFragment
                }
            }
        }
    }

    private fun deactivateTimer(savedTimerId: Int) {
        TimerOperations().killIntentAndActiveTimerEntries(this, db, savedTimerId)
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