package com.example.staysunny.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.staysunny.R
import com.example.staysunny.databinding.FragmentLoginBinding
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.view.HomeActivity
import com.example.staysunny.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()
    var isValid: Boolean = false
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        communicator = requireActivity() as OnboardingActivity
        setupView()
        setupObservers()
        return binding.root
    }

   private fun setupView() {
       binding.tvAccount.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
       }

       binding.tvForgot.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
       }

       binding.btLogin.setOnClickListener {
           requestLogin()
       }

       binding.tietEmail.addTextChangedListener {
           if(binding.tietEmail.text.toString().isEmpty()){
               binding.tfEmail.error = "Please introduce your email"
           }
       }
       binding.tietPassword.addTextChangedListener{
           if(binding.tietPassword.text.toString().isEmpty()) {
               binding.tfPassword.error = "Please introduce your password"
           }
       }
   }

    private fun setupObservers(){
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }
        viewModel.sessionValid.observe(viewLifecycleOwner) { validSession ->
            if (validSession) {
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(activity, "Invalid access", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLogin() {
        viewModel.requestLogin(binding.tietEmail.text.toString(),
            binding.tietPassword.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
