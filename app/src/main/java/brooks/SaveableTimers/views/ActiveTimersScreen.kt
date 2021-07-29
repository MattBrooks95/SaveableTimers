package brooks.SaveableTimers.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import brooks.SaveableTimers.R
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.ActiveTimersScreenBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: ActiveTimersScreenBinding

class ActiveTimersScreen: AppCompatActivity() {
    private lateinit var db: AppDatabase;
    private val className: String = "ActiveTimersScreen"
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
            val timers: List<SaveableTimer> = db.activeTimerDao().getAllActiveTimers()
            timers.forEachIndexed { index, timer ->
                val activeTimer = TextView(appContext)
                activeTimer.text = "Timer #:$index timer name:${timer.displayName}"
                activeTimer.layoutParams = newLayoutParams

                binding.activeTimersContainer.addView(activeTimer)
            }
        }
    }

    private fun setHandlers() {
        Log.d(className, "setHandlers")
        val toCreateTimerScreenButton = binding.navigateCreateTimerButton
        toCreateTimerScreenButton.setOnClickListener {
            Log.d(className, "Go to make timer screen")
            val goToCreateTimerIntent = Intent(this, CreateTimerScreen::class.java)
            startActivity(goToCreateTimerIntent)
        }

        val toSavedTimersScreenButton = binding.navigateSavedTimersButton
        toSavedTimersScreenButton.setOnClickListener {
            Log.d(className, "go to saved timers screen")
            val goToSavedTimersScreenIntent = Intent(this, SavedTimersScreen::class.java)
            startActivity(goToSavedTimersScreenIntent);
        }
    }
}