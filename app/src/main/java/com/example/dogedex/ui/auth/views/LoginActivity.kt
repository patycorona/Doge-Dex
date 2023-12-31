package com.example.dogedex.ui.auth.views


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.dogedex.R
import com.example.dogedex.databinding.ActivityLoginBinding
import com.example.dogedex.domain.model.AuthModel
import com.example.dogedex.domain.model.ConstantGeneral
import com.example.dogedex.ui.component.Screen
import com.example.dogedex.ui.main.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       changeScreen(Screen.LoginFragment, AuthModel())

    }

    private fun changeFragment(fragment: Fragment) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_principal, fragment)
        ft.commit()
    }

    fun changeScreen(typeScreen: Screen,  auth: AuthModel? = AuthModel()) {
        binding.apply {
            when (typeScreen) {
                Screen.LoginActivity -> { openLoginFragment(auth) }
                Screen.LoginFragment -> { openLoginFragment(auth) }
                Screen.SingUpFragment -> { openSingUpFragment() }
                Screen.MainActivity -> { openMainActivity(auth) }
                else -> {
                    Toast.makeText(this@LoginActivity,
                        getString(R.string.text_error_not_found),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openLoginFragment( auhModel: AuthModel?) = changeFragment(LoginFragment.newInstance(auhModel))

    private fun openSingUpFragment() = changeFragment(SingUpFragment.newInstance())

    private fun openMainActivity(authModel: AuthModel?){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(ConstantGeneral.USER_KEY,authModel)
        startActivity(intent)
    }
}