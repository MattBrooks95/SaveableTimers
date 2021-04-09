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


class SavedTimerPanel: Fragment(R.layout.saved_timer_panel) {
    lateinit var savedTimerName: String
    lateinit var savedTimerDescription: String
    private var savedTimerId: Int = 0
    private var savedTimerDuration: Int = 0

    private lateinit var deleteCallback: (savedTimerId: Int) -> Unit?
    private lateinit var editCallback: (savedTimerId: Int) -> Unit?
    private lateinit var activateCallback: (savedTimerId: Int) -> Unit?
    private lateinit var deactivateCallback: (savedTimerId: Int) -> Unit?

    //only valid in between onCreateView and onDestroyView
    private var _binding: SavedTimerPanelBinding? = null
    private val binding get() = _binding!!

    private var isActivated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavedTimerPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        populateFields()
        setHandlers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setHandlers() {
        if (deleteCallback !== null) {
            binding.deleteButton.setOnClickListener {
                deleteCallback(savedTimerId)
            }
        }

        if (editCallback !== null) {
            binding.editButton.setOnClickListener {
                editCallback(savedTimerId)
            }
        }

        binding.toggleActiveButton.setOnClickListener{
            if (isActivated) {
                deactivate()
            } else {
                activate()
            }
            isActivated = !isActivated
        }
    }

    private fun populateFields() {
        savedTimerName = arguments?.getString("name") ?: ""
        savedTimerDescription = arguments?.getString("description") ?: ""
        savedTimerId = arguments?.getInt("id") ?: 0
        savedTimerDuration = arguments?.getInt("duration") ?: 0

        binding.nameField.text = savedTimerName
        binding.descriptionField.text = savedTimerDescription
        binding.durationField.text = savedTimerDuration.toString()
    }

    fun activate() {
        binding.editButton.isEnabled = false
        binding.deleteButton.isEnabled = false
        binding.toggleActiveButton.text = "D"
        if (::activateCallback.isInitialized) {
            activateCallback(savedTimerId)
        }
    }

    fun deactivate() {
        binding.editButton.isEnabled = true
        binding.deleteButton.isEnabled = true
        binding.toggleActiveButton.text = "A"
        if (::deactivateCallback.isInitialized) {
            deactivateCallback(savedTimerId)
        }
    }


//
    fun setDeleteButtonCallbackProperty(onClick: (uid: Int) -> Unit) {
        deleteCallback = onClick
    }

    fun setEditButtonCallbackProperty(onClick: (uid: Int) -> Unit) {
        editCallback = onClick
    }

    fun setActivateButtonCallback(onClick: (uid: Int) -> Unit) {
        activateCallback = onClick
    }

    fun setDeactivateButtonCallback(onClick: (uid: Int) -> Unit) {
        deactivateCallback = onClick
    }
}