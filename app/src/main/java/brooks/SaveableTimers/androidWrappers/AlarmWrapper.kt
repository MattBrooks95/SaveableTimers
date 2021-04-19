package brooks.SaveableTimers.androidWrappers

import android.app.AlarmManager
import android.content.Context
import brooks.SaveableTimers.data.AppDatabase

class AlarmWrapper {
    companion object{
        @Volatile private var instance: AlarmManager? = null;
        fun getInstance(context: Context): AlarmManager {
            if (instance == null) {
                instance = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            }
            return instance as AlarmManager
        }
    }
}