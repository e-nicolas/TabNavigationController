package io.github.enicolas.tabnavigationcontrollerexample.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.enicolas.tabnavigationcontrollerexample.R
import io.github.enicolas.tabnavigationcontrollerexample.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textTitle.text = it
        })

        binding.btnNavigate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_second)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}