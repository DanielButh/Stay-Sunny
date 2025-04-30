package com.example.staysunny.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.staysunny.databinding.FragmentPermissionBinding
import com.example.staysunny.utils.FragmentCommunicator

class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PermissionViewModel by viewModels()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.fbContinue.setOnClickListener {
            viewModel.triggerSuccessMessage()
        }
    }

    private fun setupObservers() {
        viewModel.mensaje.observe(viewLifecycleOwner) { success ->
            if (success) {
                showSuccessMessage()
                viewModel.resetSuccessMessage()
            }
        }
    }

    private fun showSuccessMessage() {
        Toast.makeText(
            requireContext(),
            "Successful access!",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
