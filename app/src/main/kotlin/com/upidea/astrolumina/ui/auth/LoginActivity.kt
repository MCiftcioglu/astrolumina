package com.upidea.astrolumina.ui.auth

import java.util.TimeZone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.databinding.ActivityLoginBinding
import com.upidea.astrolumina.ui.HomeActivity
import com.upidea.astrolumina.utils.AstroUtils
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
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.login(email, password) { success ->
                if (success) {
                    val user = viewModel.loggedInUser
                    val sharedPref = getSharedPreferences("AstroPrefs", MODE_PRIVATE)

                    // Burçları doğum bilgilerine göre tekrar hesapla
                    val (sun, moon, rising) = AstroUtils.calculateSignsViaPython(
                        this,
                        user?.birthDate ?: "",
                        user?.birthTime ?: "",
                        user?.birthPlace ?: ""
                    )

                    sharedPref.edit().apply {
                        putBoolean("isLoggedIn", true)
                        putString("name", user?.name)
                        putString("birthDate", user?.birthDate)
                        putString("birthTime", user?.birthTime)
                        putString("birthPlace", user?.birthPlace)
                        putString("gender", user?.gender)
                        putString("sunSign", sun)
                        putString("moonSign", moon)
                        putString("risingSign", rising)
                        apply()
                    }

                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Giriş başarısız", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
