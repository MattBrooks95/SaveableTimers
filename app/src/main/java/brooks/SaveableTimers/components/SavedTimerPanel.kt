package brooks.SaveableTimers.components

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import brooks.SaveableTimers.data.SaveableTimer
import com.google.android.material.button.MaterialButton

class SavedTimerPanel(val appContext: Context, val savedTimerData: SaveableTimer) : LinearLayout(appContext) {
    lateinit var deleteButtonReference: View
    lateinit var editButtonReference: View

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

    private fun makeDeleteButton(savedTimerId: Int): MaterialButton {
        val deleteButton = MaterialButton(appContext)
        deleteButton.text = "X"
        return deleteButton
    }

    private fun makeEditButton(savedTimerId: Int): MaterialButton {
        val makeEditButton = MaterialButton(appContext)
        makeEditButton.text = "E"
        return makeEditButton
    }

    fun setDeleteButtonCallback(onClick: (uid: Int) -> Unit) {
        deleteButtonReference.setOnClickListener {
            onClick(savedTimerData.uid)
        }
    }

    fun setEditButtonCallback(onClick: (uid: Int) -> Unit) {
        editButtonReference.setOnClickListener {
            onClick(savedTimerData.uid)
        }
    }

    init {
        val displayName = makeDisplayNameElement(savedTimerData.displayName)
        val duration = makeDurationElement(savedTimerData.duration)
        val description = makeDescriptionElement(savedTimerData.description)

        val deleteButton = makeDeleteButton(savedTimerData.uid)
        deleteButtonReference = deleteButton

        val editButton = makeEditButton(savedTimerData.uid)
        editButtonReference = editButton

        this.tag = savedTimerData

        listOf(displayName, duration, description, editButton, deleteButton).forEach {
            this.addView(it)
        }
    }
}