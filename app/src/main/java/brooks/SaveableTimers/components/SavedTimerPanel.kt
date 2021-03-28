package brooks.SaveableTimers.components

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import brooks.SaveableTimers.R
import brooks.SaveableTimers.data.SaveableTimer
import brooks.SaveableTimers.databinding.ActivitySavedTimersScreenBinding
import com.google.android.material.button.MaterialButton
import brooks.SaveableTimers.databinding.SavedTimerPanelBinding


class SavedTimerPanel(/*val appContext: Context, val savedTimerData: SaveableTimer*/) : Fragment(R.layout.saved_timer_panel) {
    lateinit var savedTimerName: String
    lateinit var savedTimerDescription: String
    private var savedTimerId: Int = 0
    private var savedTimerDuration: Int = 0

    //only valid in between onCreateView and onDestroyView
    private var _binding: SavedTimerPanelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavedTimerPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        populateFields()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        savedTimerName = args?.getString("name") ?: ""
        savedTimerDescription = args?.getString("description") ?: ""
        //gotta be a better way to declare these...
        savedTimerId = args?.getInt("id") ?: 0
        savedTimerDuration = args?.getInt("duration") ?: 0

        populateFields()
    }

    private fun populateFields() {
        binding.nameField.text = savedTimerName
        binding.durationField.text = savedTimerDuration.toString()
        binding.descriptionField.text = savedTimerDescription
    }

//    fun activate() {
//        deleteButtonReference.isEnabled = false
//        editButtonReference.isEnabled = false
//        this.removeView(activateButtonReference)
//        this.addView(deactivateButtonReference, 0)
//    }

//    fun deactivate() {
//        deleteButtonReference.isEnabled = true
//        editButtonReference.isEnabled = true
//        this.removeView(deactivateButtonReference)
//        this.addView(activateButtonReference, 0)
//    }

//    private fun makeDisplayNameElement(displayName: String?): View {
//        val newTextView = TextView(appContext)
//        newTextView.text = if (!displayName.isNullOrEmpty()) displayName else ""
//        return newTextView
//    }
//
//    private fun makeDurationElement(duration: Int): View {
//        val durationDisplayView = TextView(appContext)
//        durationDisplayView.text = duration.toString()
//        return durationDisplayView
//    }
//
//    private fun makeDescriptionElement(description: String?): View {
//        return makeDisplayNameElement(description)
//    }
//
//    private fun makeDeleteButton(savedTimerId: Int): MaterialButton {
//        return makeButton("D")
//    }
//
//    private fun makeEditButton(savedTimerId: Int): MaterialButton {
//        return makeButton("E")
//    }
//
//    private fun makeButtonWithBackgroundColor(text: String, backgroundColor: Int): MaterialButton {
//        val newButton = makeButton(text)
//        newButton.setBackgroundColor(backgroundColor)
//        return newButton
//    }
//
//    private fun makeButton(text: String): MaterialButton {
//        val newButton = MaterialButton(appContext)
//        newButton.text = text
//        return newButton
//    }

//    private fun makeActivateButton(): MaterialButton {
//        val activateButton = MaterialButton(appContext);
//        activateButton.text = "A"
//        return activateButton
//        return makeButton("A")
//    }

//    private fun makeDeactivateButton(text: String, backgroundColor: Int): MaterialButton {
//        return makeButtonWithBackgroundColor(text, backgroundColor)
//    }
//
//    fun setActivateButtonCallback(onClick: (uid: Int) -> Unit) {
//        activateButtonReference.setOnClickListener {
//            onClick(savedTimerData.uid)
//        }
//    }
//
//    fun setDeleteButtonCallback(onClick: (uid: Int) -> Unit) {
//        deleteButtonReference.setOnClickListener {
//            onClick(savedTimerData.uid)
//        }
//    }
//
//    fun setEditButtonCallback(onClick: (uid: Int) -> Unit) {
//        editButtonReference.setOnClickListener {
//            onClick(savedTimerData.uid)
//        }
//    }
//
//    fun setDeactivateButtonCallback(onClick: (uid: Int) -> Unit) {
//        deactivateButtonReference.setOnClickListener {
//            onClick(savedTimerData.uid)
//        }
//    }

//    init {
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//        val contentsView = inflater.inflate(R.layout.saved_timer_panel)
//        LayoutInflater.from(appContext).inflate(R.layout.saved_timer_panel, this)
//        val displayName = makeDisplayNameElement(savedTimerData.displayName)
//        val duration = makeDurationElement(savedTimerData.duration)
//        val description = makeDescriptionElement(savedTimerData.description)
//
//        val deleteButton = makeDeleteButton(savedTimerData.uid)
//        deleteButtonReference = deleteButton
//
//        val editButton = makeEditButton(savedTimerData.uid)
//        editButtonReference = editButton
//
//        val activateButton = makeActivateButton()
//        activateButtonReference = activateButton
//
//        val deactivateButton = makeDeactivateButton("D", ContextCompat.getColor(appContext, R.color.stop))
//        deactivateButtonReference = deactivateButton
//
//        this.tag = savedTimerData
//
//        listOf(
//            activateButton,
//            displayName,
//            duration,
//            description,
//            editButton,
//            deleteButton
//        ).forEach {
//            this.addView(it)
//        }
//    }
}