package brooks.SaveableTimers.brooks.SaveableTimers.views

import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import brooks.SaveableTimers.R

abstract class SaveableTimersBaseActivity: AppCompatActivity() {
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.swap_locale_button-> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(applicationContext).inflate(R.menu.locale_menu, menu)
        return true
    }
}