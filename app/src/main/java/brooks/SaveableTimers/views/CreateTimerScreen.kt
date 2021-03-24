package brooks.SaveableTimers.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.room.Room
import androidx.room.RoomDatabase
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.ActivityCreateTimerScreenBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private lateinit var binding: ActivityCreateTimerScreenBinding

class CreateTimerScreen : AppCompatActivity() {
    private var scope: CoroutineScope = MainScope()
    private val className: String = "CreateTimerScreen"
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTimerScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)

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
            //the ID is auto generated, but when you make an instance of the DAO you have to specify a value, so setting the int to 0
            //is necessary. I had it set to 1, and it would crash because it would use 1 on the insert, and violate primary key uniqueness
            var newSaveableTimer = SaveableTimer(0, binding.timerNameField.text.toString(), getDurationFloatFromEditableText(durationTextInput))
            scope.launch {
                timerDao.insertAll(newSaveableTimer)
            }
            Log.d(className, "made a saveable timer entry")
            val intent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(intent)
        }
    }

    private fun createDurationButtons() {
        val buttonContainer = binding.timerDurationButtonsContainer
        val buttonSettings: Array<Pair<String, Int>> = arrayOf(Pair("+20", 20), Pair("+10", 10), Pair("+5", 5), Pair("+1", 1))
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