package com.example.staysunny.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentResetPasswordBinding
import com.example.staysunny.viewModel.ResetPasswordViewModel
import com.example.staysunny.utils.FragmentCommunicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResetPasswordViewModel by viewModels()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.btnReset.setOnClickListener {
            val email = binding.tietEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "The email field cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Invalid mail format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.sendPasswordResetEmail(email)
        }

        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }

    private fun setupObservers() {
        // Observamos el estado del loader (si está cargando o no)
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)  // Usamos el loader para mostrar u ocultar
        }

        // Observamos el estado de la respuesta del envío del correo
        viewModel.passwordResetState.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Si el envío fue exitoso, mostramos el mensaje de éxito
                Toast.makeText(requireContext(), "Mail sent. Check your inbox", Toast.LENGTH_LONG).show()

                // Después de 2 segundos, navega al login
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                }, 2000)
            } else {
                // Si hubo un error, mostramos el mensaje de error
                Toast.makeText(requireContext(), "Failed to send the recovery email", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
