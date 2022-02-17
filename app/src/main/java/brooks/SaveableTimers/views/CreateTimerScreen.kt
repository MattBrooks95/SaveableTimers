package brooks.SaveableTimers.views

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.Intent.CATEGORY_OPENABLE
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.brooks.SaveableTimers.views.SaveableTimersBaseActivity
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.data.SoundFile
import brooks.SaveableTimers.databinding.ActivityCreateTimerScreenBinding
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

private lateinit var binding: ActivityCreateTimerScreenBinding

class CreateTimerScreen : SaveableTimersBaseActivity() {
    private var scope: CoroutineScope = MainScope()
    private val className: String = "CreateTimerScreen"
    lateinit var db: AppDatabase
    private val FILE_PICK_REQUEST_CODE = 1

    private var newSoundFilePath: String? = null

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
            val intent = IntentFactory().createGoToActiveTimersScreenIntent(this)
            startActivity(intent)
        }
        binding.createTimerButton.setOnClickListener {
            val durationTextInput = binding.durationTextInput.text
            if (durationTextInput.isNullOrEmpty()) return@setOnClickListener
            val timerDao = db.saveableTimerDao()
            //so we can access app context in the coroutine
            val appContext = this
            scope.launch {
                var alreadyExistingSoundFile: SoundFile? = null
                val soundFilePath = newSoundFilePath
                if (soundFilePath != null) {
                   alreadyExistingSoundFile = db.soundFileDao().getSoundFileForPath(soundFilePath)
                }

                var soundFileIdForInsert: Int? = null
                if (alreadyExistingSoundFile != null) {
                    soundFileIdForInsert = alreadyExistingSoundFile.uid
                } else if(soundFilePath != null) {
                    val newSoundFile = SoundFile(0, soundFilePath)
                    soundFileIdForInsert = db.soundFileDao().insert(newSoundFile).toInt()
//                    soundFileIdForInsert = db.soundFileDao().getMostRecentInsertId()
                }
                //the ID is auto generated, but when you make an instance of the DAO you have to specify a value, so setting the int to 0
                //is necessary. I had it set to 1, and it would crash because it would use 1 on the insert, and violate primary key uniqueness
                var newSaveableTimer = SaveableTimer(
                    0,
                    binding.timerNameField.text.toString(),
                    getDurationFloatFromEditableText(durationTextInput),
                    binding.timerDescriptionField.text.toString(),
                    soundFileIdForInsert)
                timerDao.insertAll(newSaveableTimer)
                Log.d(className, "made a saveable timer entry")
                val intent = IntentFactory().createGoToActiveTimersScreenIntent(appContext)
                startActivity(intent)
            }
        }

        binding.selectSoundFileButton.setOnClickListener {
            Log.d(className, "TODO")
            val filePickIntent = Intent(ACTION_GET_CONTENT)
            filePickIntent.addCategory(CATEGORY_OPENABLE)
            resultLauncher.launch("audio/*")
        }

        binding.useDefaultSoundFileButton.setOnClickListener {
            Log.d(className, "TODO")
        }

        binding.selectPreviousSoundFile.setOnClickListener {
            Log.d(className, "TODO")
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.d(className, "uri:" + uri?.toString())
        Log.d(className, "uri as path:" + uri?.path)
        if (uri != null) {
            val appDirectory = filesDir
            val selectedFileInputStream = contentResolver.openInputStream(uri)
            if (selectedFileInputStream != null) {
//            val inputStream: InputStream = FileInputStream(selectedFile)
                if (uri.path == null) {
                    Log.e(className, "Selected file's URI path was null????")
                } else {
                    /*anyway,so that the user can use two different sound files that may have the same file name
                     *convert the original path to a file name. This may be dumb
                     *basically something like /document/audio:20 will be converted to _document_audio_20 as a file name
                     * I can't use the real file name because I have no idea how to get it from the URI and the android docs are less than helpful
                     * if you only convert / to _, the : would make it so that the media player couldn't play the file
                     * I assume the presence of the : messed up the way the uri was parsed when the OS tried to open it
                     * I need to extract this path translation to a function that can be reused, so that when I check to see if a file has already been saved
                     * I can use the after-conversion file name
                     * I'm worried that the user selecting the same file twice could result into two different URIS somehow...
                    */
                    val outputFilePath = appDirectory.path + "/" + (uri.path as String).replace(Regex("[/:]"),"_")
                    val outputFile = File(outputFilePath)
                    selectedFileInputStream.copyTo(FileOutputStream(outputFile))
                    newSoundFilePath = outputFilePath
                    Log.d(className, "set sound file path attribute:$newSoundFilePath")
                }
            } else {
                Log.e(className, "TODO FILE OPERATIONS FAILED, USE DEFAULT")
            }
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//    }

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