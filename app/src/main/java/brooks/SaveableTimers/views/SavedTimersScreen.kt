package brooks.SaveableTimers.views

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.room.Room
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer

import brooks.SaveableTimers.databinding.ActivitySavedTimersScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private lateinit var binding: ActivitySavedTimersScreenBinding


class SavedTimersScreen : AppCompatActivity() {
    lateinit var db: AppDatabase
    private val className: String = "SavedTimersScreen"
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
            timers.forEachIndexed {_, timer ->
                val displayNameView = TextView(appContext)
                displayNameView.text = timer.displayName + "shrek"

                val durationNameView = TextView(appContext)
                val duration = timer.duration
                if (duration !== null) durationNameView.text = duration.toString()

                binding.savedTimersContainer.addView(displayNameView)
                binding.savedTimersContainer.addView(durationNameView)
            }
        }
//        val timersArray: List<SaveableTimer> = loadTimers()
//        timersArray.forEach {
//
//        }
    }

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
}