package brooks.SaveableTimers.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import brooks.SaveableTimers.R
import brooks.SaveableTimers.databinding.ActivityCreateTimerScreenBinding

private lateinit var binding: ActivityCreateTimerScreenBinding

class CreateTimerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTimerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHandlers()
    }

    private fun setHandlers() {
        binding.cancelTimerCreation.setOnClickListener {
            val intent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(intent)
        }
        binding.createTimerButton.setOnClickListener {
            val intent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(intent)
        }
    }
}