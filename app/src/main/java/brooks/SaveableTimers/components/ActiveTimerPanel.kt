package brooks.SaveableTimers.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import brooks.SaveableTimers.R
import brooks.SaveableTimers.databinding.ActiveTimerPanelBinding


class ActiveTimerPanel: Fragment(R.layout.active_timer_panel) {
    lateinit var savedTimerName: String
    lateinit var savedTimerDescription: String
    private var savedTimerId: Int = 0
    //TODO store and retreive the ring time from the DB this is an attribute of the active timer, not the saved timer itself which only cares about holding a duration
    private var activeTimerRingTime: Int = 0

    private lateinit var deactivateCallback: (savedTimerId: Int) -> Unit?


    //only valid in between onCreateView and onDestroyView
    private var _binding: ActiveTimerPanelBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ActiveTimerPanelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        populateFields()
        setHandlers()
    }

    //TODO put this in a parent class for reuse between active and saved timer panels
    private fun populateFields() {
        savedTimerName = arguments?.getString("name") ?: ""
        savedTimerDescription = arguments?.getString("description") ?: ""
        savedTimerId = arguments?.getInt("id") ?: 0
        activeTimerRingTime = arguments?.getInt("ringTime") ?: 0
        binding.nameField.text = savedTimerName
        binding.descriptionField.text = savedTimerDescription
    }

    private fun setHandlers() {
        if (::deactivateCallback.isInitialized) {
            binding.deactivateButton.setOnClickListener {
                deactivateCallback(savedTimerId)
            }
        }
    }

    fun setDeactivateButtonCallback(onClick: (uid: Int) -> Unit) {
        deactivateCallback = onClick
    }
}