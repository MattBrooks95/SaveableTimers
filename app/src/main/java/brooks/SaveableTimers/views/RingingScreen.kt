package brooks.SaveableTimers.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.RingingViewBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: RingingViewBinding

val scope = MainScope()
class RingingScreen : AppCompatActivity() {
    private val className: String = "RingingScreen"
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RingingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        Log.d(className, "view in response to alarm")
        db = AppDatabase.getInstance(this)

        val savedTimerId: Int? = savedInstanceState?.getInt("saved_timer_id")
        if (savedTimerId == null) {
            Log.d(className, "saved timer id from bundle wasn't found")
            return
        }

        scope.launch {
            val savedTimer: SaveableTimer = getSavedTimerFromDb(savedTimerId)

            populateTextFields(savedTimer)
        }
    }

    private fun populateTextFields(saveableTimer: SaveableTimer){
        binding.alarmDescription.text = saveableTimer.description
        binding.alarmName.text = saveableTimer.displayName
    }

    private suspend fun getSavedTimerFromDb(uuid: Int): SaveableTimer {
        return db.saveableTimerDao().getSaveableTimerById(uuid)
    }
}