package brooks.SaveableTimers.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import brooks.SaveableTimers.R

class SavedTimersScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_timers_screen)
    }
}