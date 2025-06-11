package com.example.staysunny.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import androidx.core.widget.addTextChangedListener
import com.example.staysunny.databinding.FragmentPersonalInformationVariantBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.PersonalInformationVariantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInformationVariantFragment : Fragment() {

    private var _binding: FragmentPersonalInformationVariantBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PersonalInformationVariantViewModel>()
    private lateinit var communicator: FragmentCommunicator
    var isValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInformationVariantBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.btContinue.setOnClickListener {
            if (isValid) {
                requestPersonalInformation()
            } else {
                Toast.makeText(activity, "Please introduce your information.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tietName.addTextChangedListener {
            if (binding.tietName.text.toString().isEmpty()) {
                binding.tfName.error = "Please introduce your name"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.tietLastNames.addTextChangedListener {
            if (binding.tietLastNames.text.toString().isEmpty()) {
                binding.tfLastNames.error = "Please introduce your Lastnames"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.tietUsername.addTextChangedListener {
            if (binding.tietUsername.text.toString().isEmpty()) {
                binding.tfUsername.error = "Please introduce your Username"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.tietBirthDate.addTextChangedListener {
            if (binding.tietBirthDate.text.toString().isEmpty()) {
                binding.tfBirthName.error = "Please introduce your Birthdate"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.tietCode.addTextChangedListener {
            if (binding.tietCode.text.toString().isEmpty()) {
                binding.tfCode.error = "Please introduce your Referral code"
                isValid = false
            } else {
                isValid = true
            }
        }
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { validSession ->
            if (validSession) {
                Toast.makeText(activity, "Valid access!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_personalInformationVariant_to_permission)
            } else {
                Toast.makeText(activity, "Please introduce your information", Toast.LENGTH_SHORT).show()
            }
        }

        }


    private fun requestPersonalInformation() {
        viewModel.requestPersonalInformation(
            binding.tietName.text.toString(),
            binding.tietUsername.text.toString(),
            binding.tietLastNames.text.toString(),
            binding.tietBirthDate.text.toString(),
            binding.tietCode.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
