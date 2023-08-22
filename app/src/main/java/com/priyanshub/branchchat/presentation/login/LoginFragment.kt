package com.priyanshub.branchchat.presentation.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.priyanshub.branchchat.R
import com.priyanshub.branchchat.common.Constants
import com.priyanshub.branchchat.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.TOKEN,null)
        if (token != null){
            Log.d("PRI", "onViewCreated: $token")
            findNavController().navigate(R.id.action_loginFragment_to_messageThreadFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreference =  requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        viewModel.showProgress.observe(viewLifecycleOwner) { showProgress ->
            binding.progressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
        }

        viewModel.loginResponse.observe(viewLifecycleOwner){
            if (it != null){
                sharedPreference.edit().putString(Constants.TOKEN,it.authToken).apply()
                Log.d("PRI", "onViewCreated: ${it.authToken}")
                findNavController().navigate(R.id.action_loginFragment_to_messageThreadFragment)
            }
            else{
                Toast.makeText(requireContext(),"Invalid username or password",Toast.LENGTH_SHORT).show()
                Log.d("PRI", "onViewCreated: NULL")
            }
        }

        binding.btnLogin.setOnClickListener {
            val userName = binding.etUserName.text.toString()
            val pass =  binding.etPassword.text.toString()
            viewModel.login(userName,pass)
        }

    }
}