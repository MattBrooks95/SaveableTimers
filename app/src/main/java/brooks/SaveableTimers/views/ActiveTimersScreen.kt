package brooks.SaveableTimers.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import brooks.SaveableTimers.R
import brooks.SaveableTimers.databinding.ActiveTimersScreenBinding

private lateinit var binding: ActiveTimersScreenBinding

class ActiveTimersScreen: AppCompatActivity() {
    private val className: String = "ActiveTimersScreen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiveTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setHandlers()
    }

    private fun setHandlers() {
        Log.d(className, "setHandlers")
        val toCreateTimerScreenButton = binding.navigateCreateTimerButton
        toCreateTimerScreenButton.setOnClickListener {
            Log.d(className, "Go to make timer screen")
        }

        val toSavedTimersScreenButton = binding.navigateSavedTimersButton
        toSavedTimersScreenButton.setOnClickListener {
            Log.d(className, "go to saved timers screen")
        }
    }
}