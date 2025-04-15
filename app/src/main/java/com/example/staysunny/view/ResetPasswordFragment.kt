package com.example.staysunny.view

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private var _binding: FragmentResetPasswordBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        setupView()
        return binding.root

    }

    private fun setupView() {
        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

        binding.btnReset.setOnClickListener {
            val email = binding.tietEmail.text.toString().trim()

            // Limpiar errores previos
            binding.textField.error = null

            // Validar campo vacío
            if (email.isEmpty()) {
                binding.textField.error = "Se requiere que llene el campo"
                return@setOnClickListener
            }

            // Validar formato de correo
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.textField.error = "Ingrese un correo electrónico válido"
                return@setOnClickListener
            }

            // Limpiar errores si todo está bien
            binding.textField.error = null

            // Mostrar loader
            binding.loaderView.visibility = View.VISIBLE
            binding.loaderView.playAnimation()

            // Simular operación asíncrona
            binding.btnReset.postDelayed({
                binding.loaderView.cancelAnimation()
                binding.loaderView.visibility = View.GONE
                // Mostrar mensaje
                Toast.makeText(requireContext(), "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
            }, 2000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}