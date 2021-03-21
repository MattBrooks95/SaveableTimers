package brooks.SaveableTimers.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer

import brooks.SaveableTimers.databinding.ActivitySavedTimersScreenBinding

private lateinit var binding: ActivitySavedTimersScreenBinding


class SavedTimersScreen : AppCompatActivity() {
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setHandlers()

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        ).build()
        setHandlers()

        val timersArray: List<SaveableTimer> = loadTimers()
        timersArray.forEach {
            val displayNameView = TextView(this)
            displayNameView.text = it.displayName

            val durationNameView = TextView(this)
            val duration = it.duration
            if (duration !== null) durationNameView.text = duration.toString()

            binding.savedTimersContainer.addView(displayNameView)
            binding.savedTimersContainer.addView(durationNameView)
        }
    }

    private fun setHandlers() {
        val goToActiveTimersScreenButton = binding.navigateActiveTimersButton
        goToActiveTimersScreenButton.setOnClickListener {
            val goToActiveTimersScreenIntent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(goToActiveTimersScreenIntent)
        }
    }

    private fun loadTimers(): List<SaveableTimer> {
        return db.saveableTimerDao().getAll()
    }
}