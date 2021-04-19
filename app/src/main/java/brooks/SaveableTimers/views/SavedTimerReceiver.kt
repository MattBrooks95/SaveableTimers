package brooks.SaveableTimers.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import brooks.SaveableTimers.databinding.SavedTimerReceiverBinding

private lateinit var binding: SavedTimerReceiverBinding

class SavedTimerReceiver : AppCompatActivity() {
    private val className: String = "SavedTimerReceiver"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SavedTimerReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(className, "the alarm was fired!")
    }
}
