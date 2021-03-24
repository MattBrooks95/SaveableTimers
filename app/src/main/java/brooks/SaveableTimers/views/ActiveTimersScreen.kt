package brooks.SaveableTimers.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import brooks.SaveableTimers.R
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.databinding.ActiveTimersScreenBinding

private lateinit var binding: ActiveTimersScreenBinding

class ActiveTimersScreen: AppCompatActivity() {
    private lateinit var db: AppDatabase;
    private val className: String = "ActiveTimersScreen"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiveTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        setHandlers()
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