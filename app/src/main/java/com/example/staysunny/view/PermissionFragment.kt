package com.example.staysunny.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.staysunny.R
import androidx.navigation.fragment.findNavController
import com.example.staysunny.databinding.FragmentPermissionBinding
import com.example.staysunny.viewModel.PermissionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PermissionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }


    private fun setupView() {
        binding.fbContinue.setOnClickListener {
            viewModel.triggerSuccessMessage()
        }
        binding.fbContinue.setOnClickListener {
            findNavController().navigate(R.id.action_permission_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}