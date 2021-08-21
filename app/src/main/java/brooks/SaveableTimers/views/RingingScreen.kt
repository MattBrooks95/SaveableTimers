package brooks.SaveableTimers.views

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import brooks.SaveableTimers.Intents.IntentFactory
import brooks.SaveableTimers.Operations.TimerOperations
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.RingingViewBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

private lateinit var binding: RingingViewBinding

val scope = MainScope()
class RingingScreen : AppCompatActivity() {
    private val className: String = "RingingScreen"
    lateinit var db: AppDatabase
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    var savedTimerId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RingingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        Log.d(className, "view in response to alarm")

        savedTimerId = intent.getIntExtra("saved_timer_id", -1)
        if (savedTimerId == -1) {
            Log.d(className, "saved timer id from bundle wasn't found")
            return
        }

        scope.launch {
            val savedTimer: SaveableTimer = getSavedTimerFromDb(savedTimerId)
            if (savedTimer.soundFileId != null) {
                val soundFilePath = getSoundFilePathForId(savedTimer.soundFileId)
                populateTextFields(savedTimer)
                ring(soundFilePath);
            } else {
                Log.v(className,"::onCreate saveable timer has a null sound file id, need to use default ringer")
            }
        }
        TimerOperations().deactivateTimer(this, db, savedTimerId)
        setHandlers();
    }

    private suspend fun getSoundFilePathForId(soundFileId: Int): String {
        return db.soundFileDao().getSoundFileForId(soundFileId).uriPath
    }

    private fun setHandlers(){
        binding.dismissButton.setOnClickListener {
            dismissAlarm();
        }
    }

    private fun dismissAlarm() {
        mediaPlayer?.release()
        mediaPlayer = null
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val alarmDismissedIntent = IntentFactory().createTimerWasDismissedIntent(this, savedTimerId)
        localBroadcastManager.sendBroadcast(alarmDismissedIntent)
        finish();
    }

    private fun ring(soundFilePath: String?) {
        var soundFilePathToUse: String? = null;
        if (soundFilePath != null) {
            soundFilePathToUse = soundFilePath
        } else {
            soundFilePathToUse = "TODO_DEFAULT_FILE_PATH"
            Log.w(className, "sound file path was defaulted on the ringing screen!")
        }
        try {
            mediaPlayer?.isLooping = true
            mediaPlayer?.apply {
                setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType((AudioAttributes.CONTENT_TYPE_MUSIC))
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build()
                )
                setDataSource(applicationContext, Uri.fromFile(File(soundFilePathToUse)))
                prepare()
                start()
            }
        } catch (error: IOException) {
            Log.d("RingingScreen", "starting the media player failed:" + error.message)
        }
    }

    private fun populateTextFields(saveableTimer: SaveableTimer){
        binding.alarmDescription.text = saveableTimer.description
        binding.alarmName.text = saveableTimer.displayName
        Log.d(className, "sound file id?:" + saveableTimer.soundFileId)
    }

    private suspend fun getSavedTimerFromDb(uuid: Int): SaveableTimer {
        return db.saveableTimerDao().getSaveableTimerById(uuid)
    }
}