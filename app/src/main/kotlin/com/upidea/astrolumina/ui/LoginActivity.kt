package com.upidea.astrolumina.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.data.entity.UserEntity
import com.upidea.astrolumina.databinding.ActivityLoginBinding
import com.upidea.astrolumina.ui.HomeActivity
import com.upidea.astrolumina.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.getUserByEmail(email).observe(this) { user ->
                    if (user != null && user.password == password) {
                        goToHome(user)
                    } else {
                        Toast.makeText(this, "E-posta veya şifre hatalı", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun goToHome(user: UserEntity) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("user_id", user.id)
            putExtra("user_name", user.name)
            putExtra("user_gender", user.gender)
        }
        startActivity(intent)
        finish()
    }
}
