package brooks.SaveableTimers.brooks.SaveableTimers.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import brooks.SaveableTimers.AppState
import brooks.SaveableTimers.R
import java.util.*

abstract class SaveableTimersBaseActivity: AppCompatActivity() {
    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
        //the api for changing the locale isn't documented and sucks shit
        //why should I have to do this? https://stackoverflow.com/questions/39705739/android-n-change-language-programmatically/40849142#40849142
        //I'm going to drop in-app language setting for now. It's not worth it.
        //Log.d(TAG, "onCreate, locale:${resources.configuration.locales}")
        //Log.d(TAG, "onCreate, bundle:$savedInstanceBundle locale:${resources.configuration.locales.get(0)}")
        //savedInstanceBundle?.let {
        //    var locale: String? = it.get(LOCALE_KEY) as String?
        //    locale?.let { locale ->
        //        var newLocale: Locale =
        //            if (locale == "ja_JP") {
        //                AppState.getInstance().localeToJapanese()
        //                Locale.JAPAN
        //            } else {
        //                AppState.getInstance().localeToEnglish()
        //                Locale.US
        //            }
        //        Log.d(TAG, "setting to locale:$newLocale")
        //        resources.configuration.setLocale(newLocale)
        //    }

        //}
    //}

    //override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //    return when (item.itemId) {
    //        R.id.swap_locale_button-> {
    //            //invalidateOptionsMenu()
    //            var appState = AppState.getInstance()
    //            val newLocale = Locale(appState.currentLanguage().value)
    //            Locale.setDefault(newLocale)
    //            resources.configuration.setLocale(Locale.getDefault())
    //            //this answer from stacked overflow doesn't seem to work
    //            //val configuration = resources.configuration;
    //            //configuration.locale = newLocale
    //            ////resources.configuration.setLocale(newLocale);
    //            //resources.updateConfiguration(configuration, resources.displayMetrics)
    //            ////recreate()
    //            true
    //        }
    //        else -> super.onOptionsItemSelected(item)
    //    }
    //}

    //override fun onSaveInstanceState(outState: Bundle) {
    //    var saveLocale = AppState.getInstance().currentLocale()
    //    Log.d(TAG, "setting locale in bundle:$saveLocale")
    //    outState.putString(LOCALE_KEY, AppState.getInstance().currentLocale().toString())
    //    super.onSaveInstanceState(outState)
    //}

    //override fun onCreateOptionsMenu(menu: Menu): Boolean {
    //    MenuInflater(applicationContext).inflate(R.menu.locale_menu, menu)
    //    return true
    //}

    //companion object {
    //    var LOCALE_KEY = "LOCALE"
    //    var TAG = "SaveableTimersBaseActivity"
    //}
}