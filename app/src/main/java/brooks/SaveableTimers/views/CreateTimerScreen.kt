package brooks.SaveableTimers.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.LinearLayout
import androidx.room.Room
import androidx.room.RoomDatabase
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.ActivityCreateTimerScreenBinding
import com.google.android.material.button.MaterialButton

private lateinit var binding: ActivityCreateTimerScreenBinding

class CreateTimerScreen : AppCompatActivity() {
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTimerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "database-name"
        ).build()
        setHandlers()
        createDurationButtons()
    }

    private fun setHandlers() {
        binding.cancelTimerCreation.setOnClickListener {
            val intent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(intent)
        }
        binding.createTimerButton.setOnClickListener {
            val durationTextInput = binding.durationTextInput.text
            if (durationTextInput.isNullOrEmpty()) return@setOnClickListener
            val timerDao = db.saveableTimerDao()
            var newSaveableTimer = SaveableTimer(1, binding.timerNameField.text.toString(), getDurationFloatFromEditableText(durationTextInput))
            timerDao.insertAll(newSaveableTimer)
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
            newButton.setOnClickListener { view ->
                durationIncreaseButtonHandler(view, increaseValue)
            }
            buttonContainer.addView(newButton)
        }
    }

    private fun durationIncreaseButtonHandler(view: View, increaseValue: Int) {
        val durationField = binding.durationTextInput
        val durationText = durationField.text
        if (durationText != null) {
            var durationValue: Int = getDurationFloatFromEditableText(durationText)
            durationValue += increaseValue
            durationField.setText(durationValue.toString())
        }
    }

    private fun getDurationFloatFromEditableText(editText: Editable): Int {
        val editableAsString = editText.toString()
        return if (editableAsString.isNullOrEmpty()) 0 else editableAsString.toInt()
    }
}