package brooks.SaveableTimers.components

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import brooks.SaveableTimers.R
import brooks.SaveableTimers.databinding.SavedTimerPanelBinding
import com.google.android.material.button.MaterialButton


class SavedTimerPanel(): Fragment(R.layout.saved_timer_panel) {

    private val className = "SavedTimerPanel"
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
            Log.d(className, "set is activated:$isActivated")
            field = newIsActivated
            updateStateToReflectActivationStatus()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SavedTimerPanelBinding.inflate(inflater, container, false)
        arguments?.getBoolean("is_active_panel")?.let {
            if (it) {
                binding.root.removeView(binding.root.rootView.findViewById(R.id.delete_button))
                binding.root.removeView(binding.root.rootView.findViewById(R.id.edit_button))
                //TODO seems like a stupid way to move the button over to the right....
                val deactivateButton: MaterialButton  = binding.root.rootView.findViewById(R.id.toggle_active_button)
                binding.root.removeView(deactivateButton)
                binding.root.addView(deactivateButton)
                this.initialActiveValue = true
            }
        }
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
        //TODO this could cause a bug if it was meant to have multiple listeners
        if (::deleteCallback.isInitialized && !binding.deleteButton.hasOnClickListeners()) {
            binding.deleteButton.setOnClickListener {
                deleteCallback?.invoke(savedTimerId)
            }
        }

        //TODO this could cause a bug if it was meant to have multiple listeners
        if (::editCallback.isInitialized && !binding.editButton.hasOnClickListeners()) {
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