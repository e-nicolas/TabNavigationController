package io.github.enicolas.tabnavigationcontrollerexample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.enicolas.tabnavigationcontrollerexample.R
import io.github.enicolas.tabnavigationcontrollerexample.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textTitle.text = it
        })

        binding.btnNavigate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_second)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}