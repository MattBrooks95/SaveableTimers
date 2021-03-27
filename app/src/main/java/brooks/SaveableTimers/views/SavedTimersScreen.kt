package brooks.SaveableTimers.views

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.room.Room
import brooks.SaveableTimers.components.SavedTimerPanel
import brooks.SaveableTimers.data.AppDatabase
import brooks.SaveableTimers.data.SaveableTimer

import brooks.SaveableTimers.databinding.ActivitySavedTimersScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private lateinit var binding: ActivitySavedTimersScreenBinding


class SavedTimersScreen : AppCompatActivity() {
    lateinit var db: AppDatabase
    private val className: String = "SavedTimersScreen"
    private var timerViewMap: MutableMap<Int, SavedTimerPanel> = mutableMapOf()
    val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedTimersScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setHandlers()

        db = AppDatabase.getInstance(this)
        setHandlers()

        val appContext = this

        scope.launch{
            val timers: List<SaveableTimer> = loadTimers()
            Log.d(className,"within the loop to make timer elements, length:" + timers.size)

            val newLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            timers.forEachIndexed {index, timer ->
                val savedTimerView = buildViewForSavedTimer(timer)
                timerViewMap.put(timer.uid, savedTimerView)
                if (index > 0) newLayoutParams.marginStart = 5
                binding.savedTimersContainer.addView(savedTimerView);
            }
        }
    }

    //TODO boot up create screen, pre-pop the current data and then allow them to re-save it
    private fun editSavedTimer(uuid: Int) {
        Log.d(className, "edit uuid:$uuid")
    }

    //TODO some sort of confirmation?
    private fun deleteSavedTimer(uuid: Int) {
        Log.d(className, "delete uuid:$uuid")
        val savedTimerPanel = timerViewMap[uuid];
        if (savedTimerPanel !== null) {
            scope.launch {
                val timerDao = db.saveableTimerDao()
                timerDao.deleteAll(savedTimerPanel.savedTimerData);
                binding.savedTimersContainer.removeView(savedTimerPanel)
            }
        }
    }

    private fun activateTimer(uuid: Int) {
        Log.d(className, "activate timer:$uuid")
        val savedTimerPanel = timerViewMap[uuid]
        if (savedTimerPanel !== null) {
            savedTimerPanel.activate()
        }
    }

    private fun deactivateTimer(uuid: Int) {
        Log.d(className, "deactivate timer:$uuid")
        val savedTimerPanel = timerViewMap[uuid]
        if (savedTimerPanel !== null) {
            savedTimerPanel.deactivate()
        }
    }

    private fun buildViewForSavedTimer(savedTimer: SaveableTimer): SavedTimerPanel {
        val savedTimerPanel = SavedTimerPanel(this, savedTimer)
        //this double colon syntax was necessary to pass the method as a parameter
        savedTimerPanel.setEditButtonCallback(::editSavedTimer)
        savedTimerPanel.setDeleteButtonCallback(::deleteSavedTimer)
        savedTimerPanel.setActivateButtonCallback(::activateTimer)
        savedTimerPanel.setDeactivateButtonCallback(::deactivateTimer)
        return savedTimerPanel
    }

    private fun setHandlers() {
        val goToActiveTimersScreenButton = binding.navigateActiveTimersButton
        goToActiveTimersScreenButton.setOnClickListener {
            val goToActiveTimersScreenIntent = Intent(this, ActiveTimersScreen::class.java)
            startActivity(goToActiveTimersScreenIntent)
        }
    }

    private suspend fun loadTimers(): List<SaveableTimer> {
        return db.saveableTimerDao().getAll()
    }
}