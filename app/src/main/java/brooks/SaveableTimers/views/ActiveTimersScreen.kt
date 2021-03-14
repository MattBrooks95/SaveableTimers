package brooks.SaveableTimers.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import brooks.SaveableTimers.R

class ActiveTimersScreen: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.active_timers_screen)
    }
}