package brooks.SaveableTimers.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.databinding.RingingViewBinding

private lateinit var binding: RingingViewBinding

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

        populateTextFields()
    }

    private fun populateTextFields(){
        val savedTimer = db.saveableTimerDao().getSaveableTimerById()
        binding.alarmDescription.text =
    }
}