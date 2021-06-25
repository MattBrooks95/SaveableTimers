package brooks.SaveableTimers.views

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RingingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)
        Log.d(className, "view in response to alarm")
        db = AppDatabase.getInstance(this)

        val savedTimerId: Int = intent.getIntExtra("saved_timer_id", -1)
        if (savedTimerId == -1) {
            Log.d(className, "saved timer id from bundle wasn't found")
            return
        }

        scope.launch {
            val savedTimer: SaveableTimer = getSavedTimerFromDb(savedTimerId)

            populateTextFields(savedTimer)
        }

        setHandlers();
        ring();
    }

    private fun setHandlers(){
        binding.dismissButton.setOnClickListener {
            dismissAlarm();
        }
    }

    private fun dismissAlarm() {
        mediaPlayer?.release()
        mediaPlayer = null
        finish();
    }

    private fun ring() {
        try {
            mediaPlayer?.apply {
                setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType((AudioAttributes.CONTENT_TYPE_MUSIC))
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build()
                )
                setDataSource(applicationContext, File("Phone/Music/zoinks.mp3").toUri())
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
    }

    private suspend fun getSavedTimerFromDb(uuid: Int): SaveableTimer {
        return db.saveableTimerDao().getSaveableTimerById(uuid)
    }
}