package brooks.SaveableTimers.views

import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import brooks.SaveableTimers.Operations.TimerOperations
import brooks.SaveableTimers.data.ActiveTimer
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.data.SaveableTimerWithActiveTimerEntries
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

            populateTextFields(savedTimer)
            ring(savedTimer.soundFilePath);
        }
        TimerOperations().deactivateTimer(this, db, savedTimerId)
        setHandlers();
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
        val alarmDismissedIntent = Intent()//TODO use intent factory
        alarmDismissedIntent.action = SavedTimersScreen.TIMER_WAS_DISMISSED_INTENT
        alarmDismissedIntent.putExtra(SavedTimersScreen.RINGER_INTENT_TIMER_ID, savedTimerId)
        localBroadcastManager.sendBroadcast(alarmDismissedIntent)
        finish();
    }

    private fun ring(soundFilePath: String?) {
        var soundFilePathToUse: String? = null;
        if (soundFilePath != null) {
            soundFilePathToUse = soundFilePath
        } else {
            soundFilePathToUse = "TODO_DEFAULT_FILE_PATH"
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
        Log.d(className, "sound file path?:" + saveableTimer.soundFilePath)
    }

    private suspend fun getSavedTimerFromDb(uuid: Int): SaveableTimer {
        return db.saveableTimerDao().getSaveableTimerById(uuid)
    }
}