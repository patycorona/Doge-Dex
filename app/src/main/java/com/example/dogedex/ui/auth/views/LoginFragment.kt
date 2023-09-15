package com.example.dogedex.ui.auth.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.dogedex.R
import com.example.dogedex.databinding.FragmentLoginBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral.Companion.EMPTY
import com.example.dogedex.ui.auth.viewmodel.AuthViewModel
import com.example.dogedex.ui.component.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment(
    private var authModel : AuthModel?
) : Fragment() {

    var binding : FragmentLoginBinding? = null
    private val authViewModel:AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(LayoutInflater.from(context),null,false)

        initLoginObserver()
        initListener()
        return binding?.root
    }

    private fun initListener() {

        binding?.apply {
            btnIngresar.setOnClickListener {
                validaCampos()
            }
            tvRegistro.setOnClickListener {
                (activity as LoginActivity)
                    .changeScreen(Screen.SingUpFragment)
            }
        }
    }

    private val loginResultObserver = Observer<AuthModel> { loginResult ->

        if (loginResult.authentication_token.isNotEmpty()){

             val authModel = AuthModel(loginResult.id,loginResult.email,loginResult.authentication_token)
            (activity as LoginActivity)
                .changeScreen(Screen.MainActivity, authModel)

        }else{
            Toast.makeText(requireContext(), getString(R.string.text_error_not_found), Toast.LENGTH_SHORT).show()
        }
    }

    private fun validaCampos() {
        binding?.apply {

            tiEmail.error = EMPTY
            tiPwd.error = EMPTY

            val email = edUserName.text.toString()
            if(email.isNullOrEmpty()){ //!validEmail(email)
                tiEmail.error = getString(R.string.lbl_email_error)
                return
            }
            val password = edPassword.text.toString()
            if(password.isNullOrEmpty()){
                tiPwd.error = getString(R.string.lbl_password_error)
                return
            }
            login(email,password)
        }
    }

    private fun initLoginObserver(){
        authViewModel.auth.observe(viewLifecycleOwner, loginResultObserver)
    }

    private fun login(email: String, pwd: String) {
        authViewModel.login(email, pwd)
    }

    companion object {
        @JvmStatic
        fun newInstance(auhModel: AuthModel?): LoginFragment{
            return LoginFragment(auhModel)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}