package io.github.enicolas.tabnavigationcontrollerexample.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.enicolas.tabnavigationcontrollerexample.R
import io.github.enicolas.tabnavigationcontrollerexample.databinding.FragmentNotificationsBinding
import io.github.enicolas.tabnavigationcontrollerexample.ui.second.SecondViewModel

class NotificationsFragment : Fragment() {

    private lateinit var viewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textTitle.text = it
        })

        binding.btnNavigate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_notifications_to_navigation_second)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}