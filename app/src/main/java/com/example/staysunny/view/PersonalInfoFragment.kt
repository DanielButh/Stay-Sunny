package com.example.staysunny.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.databinding.FragmentPersonalInfoBinding
import com.example.staysunny.model.User
import com.example.staysunny.viewModel.PersonalInfoViewModel
import com.example.staysunny.viewModel.PersonalInformationVariantViewModel
import com.example.staysunny.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.text.Typography.dagger

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {
    private var _binding: FragmentPersonalInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<ProfileViewModel>()
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this
        _binding = FragmentPersonalInfoBinding.inflate(inflater,container, false)
        communicator = requireActivity() as HomeActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        setupObservers()
        viewModel.getUserInfo()
        binding.bornDateTiet.apply {
            isFocusable = false
            isClickable = true
        }

        binding.saveDataButton.setOnClickListener {
            val dateString = binding.bornDateTiet.text.toString()
            val parsedDate: Date = try {
                format.parse(dateString) ?: Date()
            } catch (e: ParseException) {
                Log.e("TaskDetailFragment", "Error al parsear la fecha: ${e.message}")
                Date()
            }
                viewModel.updateUserInfo(
                    binding.userFirstNameTiet.text.toString(),
                    binding.userLastNameTiet.text.toString(),
                    binding.userNameTiet.text.toString(),
                    parsedDate)

        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_personalInfoFragment_to_profileFragment)
        }

        binding.bornDateTiet.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                // Ajusta el mes (+1 porque empieza en 0)
                val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.bornDateTiet.setText(fechaSeleccionada)
            }, year, month, day)

            datePicker.show()
        }
    }

    private fun setupObservers() {
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }
        viewModel.loaderState.observe(viewLifecycleOwner) {
            communicator.showLoader(it)

        }
        viewModel.operationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }
    private fun updateUI(user: User) {
        binding?.apply {
            userFirstNameTiet.setText(user.name)
            userLastNameTiet.setText(user.lastName)
            userNameTiet.setText(user.userName)
            bornDateTiet.setText(format.format(user.bornDate))

        }
    }
}