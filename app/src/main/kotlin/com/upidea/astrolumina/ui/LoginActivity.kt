package com.upidea.astrolumina.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.ui.HomeActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email == "test@astro.com" && password == "123456") {
                val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().apply {
                    putBoolean("isLoggedIn", true)
                    putString("userEmail", email)
                    putBoolean("isPremium", email == "test@astro.com")
                    apply()
                }

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Hatalı e-posta veya şifre", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
