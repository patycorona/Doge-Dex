package com.example.dogedex.ui.auth.views

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dogedex.R
import com.example.dogedex.data.model.response.AuthResponse
import com.example.dogedex.databinding.FragmentSingUpBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.ui.auth.viewmodel.AuthViewModel
import com.example.dogedex.ui.component.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingUpFragment : Fragment() {

    var binding: FragmentSingUpBinding? = null
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            (activity as LoginActivity)
                .changeScreen(Screen.LoginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSingUpBinding.inflate(LayoutInflater.from(context),null,false)

        initListener()
        initLoginObserver()
        return binding?.root
    }

    private fun initListener() {
        binding?.apply {
            btnRegistar.setOnClickListener {
                validaCampos()
            }
        }
    }

    private fun validaCampos() {
        binding?.apply {

            tiEmail.error = ""
            tiPwd.error = ""
            tiConfPwd.error = ""

            val email = edUserName.text.toString()
            if(email.isNullOrEmpty()){
                tiEmail.error = getString(R.string.lbl_email_error)
                return
            }
            val password = edPassword.text.toString()
            if(password.isNullOrEmpty()){
                tiPwd.error = getString(R.string.lbl_password_error)
                return
            }
            val confirmPwd = edConfPwd.text.toString()
            if(confirmPwd.isNullOrEmpty()){
                tiConfPwd.error = getString(R.string.lbl_password_error)
                return
            }
            if(password != confirmPwd){
                tiPwd.error = getString(R.string.lbl_passwords_error)
                return
            }
            userRegister(email,password,confirmPwd)
        }
    }

    private val userRegisterResultObserver = Observer<AuthModel> { userResult ->

        if (userResult.authentication_token != null){
            Toast.makeText(requireContext(), "USUARIO REGISTRADO", Toast.LENGTH_SHORT).show()
            val authModel = AuthModel(userResult.id,userResult.email,userResult.authentication_token)
            (activity as LoginActivity)
                .changeScreen(Screen.LoginActivity,authModel )
        }else{
            Toast.makeText(requireContext(), "OCURRIO UN ERROR", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initLoginObserver(){
        authViewModel.auth.observe(viewLifecycleOwner, userRegisterResultObserver)
    }

    private fun validEmail(email:String?):Boolean {
        return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun userRegister(email: String, pwd: String, confirmPwd:String) {
        authViewModel.userRegister(email, pwd, confirmPwd)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SingUpFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}