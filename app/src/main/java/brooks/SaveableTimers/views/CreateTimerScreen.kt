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
        val buttonSettings: Array<Pair<String, Int>> = arrayOf(Pair("+20", 20), Pair("+10", 10), Pair("+5", 10), Pair("+1", 1))
        val newLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0.25f
        )

        buttonSettings.forEachIndexed { index, element ->
            val displayString = element.first;
            val increaseValue = element.second;

            val newButton: MaterialButton = MaterialButton(this)

            if (index != 0 ) newLayoutParams.marginStart = 2
            newButton.layoutParams = newLayoutParams

            newButton.text = displayString
            newButton.setOnClickListener {
                val durationField = binding.durationTextInput
                val durationText = durationField.text
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