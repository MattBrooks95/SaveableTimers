package brooks.SaveableTimers.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import brooks.SaveableTimers.databinding.ActivityCreateTimerScreenBinding
import com.google.android.material.button.MaterialButton

private lateinit var binding: ActivityCreateTimerScreenBinding

class CreateTimerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTimerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setHandlers()
        createDurationButtons()
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

    private fun createDurationButtons() {
        val buttonContainer = binding.timerDurationButtonsContainer
        val durationField = binding.durationTextInput
        val durationText = durationField.text
        val buttonSettings: Array<Pair<String, Int>> = arrayOf(Pair("+20", 20), Pair("+10", 10), Pair("+5", 10), Pair("+1", 1))
        buttonSettings.forEach {
            val displayString = it.first;
            val increaseValue = it.second;

            val newButton: MaterialButton = MaterialButton(this);
            newButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                 0.25f
            )
            newButton.text = displayString
            newButton.setOnClickListener {
                if (durationText != null) {
                    val durationTextString = durationText.toString()
                    var durationValue: Float = if (durationTextString.isNullOrEmpty()) 0.0f else durationTextString.toFloat()
                    durationValue += increaseValue
                    durationField.setText(durationValue.toString())
                }
            }
            buttonContainer.addView(newButton)
        }
    }
}