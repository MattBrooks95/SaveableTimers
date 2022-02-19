package brooks.SaveableTimers.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import brooks.SaveableTimers.R
import brooks.SaveableTimers.databinding.SavedTimerPanelBinding


class SavedTimerPanel(): Fragment(R.layout.saved_timer_panel) {

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

    //so that the isActivated value can be set from outside BEFORE this fragment is done being created
    //isActivated's setter will try to update views that don't exist yet
    var initialActiveValue: Boolean = false

    private var isActivated: Boolean = false
        set(newIsActivated) {
            field = newIsActivated
            updateStateToReflectActivationStatus()
        }

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
        isActivated = initialActiveValue
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setActiveStatusFromOutside(newIsActivated: Boolean) {
        isActivated = newIsActivated
    }

    private fun updateStateToReflectActivationStatus() {
        if (isActivated) {
            setButtonStateForActive()
        } else {
            setButtonStateForNotActive()
        }

        updateToggleActiveButtonCallback()
    }

    private fun setButtonStateForActive() {
        binding.editButton.isEnabled = false
        binding.deleteButton.isEnabled = false
        binding.toggleActiveButton.text = context?.getString(R.string.deactivate)
    }

    private fun setButtonStateForNotActive() {
        binding.editButton.isEnabled = true
        binding.deleteButton.isEnabled = true
        binding.toggleActiveButton.text = context?.getString(R.string.activate_timer)
    }


    private fun setHandlers() {
        if (::deleteCallback.isInitialized && !binding.deleteButton.hasOnClickListeners()) {//TODO this could cause a bug if it was meant to have multiple listeners
            binding.deleteButton.setOnClickListener {
                deleteCallback?.invoke(savedTimerId)
            }
        }

        if (::editCallback.isInitialized && !binding.editButton.hasOnClickListeners()) {//TODO this could cause a bug if it was meant to have multiple listeners
            binding.editButton.setOnClickListener {
                editCallback?.invoke(savedTimerId)
            }
        }
    }

    private fun updateToggleActiveButtonCallback() {
        binding.toggleActiveButton.setOnClickListener {
            if (isActivated) {
                runDeactivateCallback()
            } else {
                runActivateCallback()
            }
        }
    }


    private fun populateFields() {
        savedTimerName = arguments?.getString("name") ?: ""
        savedTimerDescription = arguments?.getString("description") ?: ""
        savedTimerId = arguments?.getInt("id") ?: 0
        savedTimerDuration = arguments?.getInt("duration") ?: 0

        binding.nameField.text = savedTimerName
        binding.nameField.isSelected = true
        binding.descriptionField.text = savedTimerDescription
        binding.descriptionField.isSelected = true
        binding.durationField.text = savedTimerDuration.toString()
    }

    private fun runActivateCallback() {
        if (::activateCallback.isInitialized) {
            activateCallback(savedTimerId)
        }
        isActivated = true
    }

    private fun runDeactivateCallback() {
        if (::deactivateCallback.isInitialized) {
            deactivateCallback(savedTimerId)
        }
        isActivated = false
    }

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