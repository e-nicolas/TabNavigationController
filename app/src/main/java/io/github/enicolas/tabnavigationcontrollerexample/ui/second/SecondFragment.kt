package io.github.enicolas.tabnavigationcontrollerexample.ui.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.enicolas.tabnavigationcontrollerexample.R
import io.github.enicolas.tabnavigationcontrollerexample.databinding.FragmentNotificationsBinding
import io.github.enicolas.tabnavigationcontrollerexample.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private lateinit var viewModel: SecondViewModel
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewModel =
                ViewModelProvider(this).get(SecondViewModel::class.java)
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textTitle.text = it
        })

        binding.btnNavigate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_second_self)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}