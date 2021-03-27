package brooks.SaveableTimers.components

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import brooks.SaveableTimers.data.SaveableTimer

class SavedTimerPanel(val appContext: Context, val savedTimerData: SaveableTimer) : LinearLayout(appContext) {

    private fun makeDisplayNameElement(displayName: String?): View {
        val newTextView = TextView(appContext)
        newTextView.text = if (!displayName.isNullOrEmpty()) displayName else ""
        return newTextView
    }

    private fun makeDurationElement(duration: Int): View {
        val durationDisplayView = TextView(appContext)
        durationDisplayView.text = duration.toString()
        return durationDisplayView
    }

    private fun makeDescriptionElement(description: String?): View {
        return makeDisplayNameElement(description)
    }

    init {
        val displayName = makeDisplayNameElement(savedTimerData.displayName);
        val duration = makeDurationElement(savedTimerData.duration);
        val description = makeDescriptionElement(savedTimerData.description);

        listOf(displayName, duration, description).forEach {
            this.addView(it)
        }
    }
}