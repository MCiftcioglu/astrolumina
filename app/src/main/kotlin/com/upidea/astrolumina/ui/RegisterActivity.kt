package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.utils.getSunSign  // EKLENDİ

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val editName = findViewById<EditText>(R.id.editTextName)
        val editBirthDate = findViewById<EditText>(R.id.editTextBirthDate)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val name = editName.text.toString()
            val birthDate = editBirthDate.text.toString()

            // GG/AA/YYYY formatında bekleniyor
            val parts = birthDate.split("/")
            val sunSign = if (parts.size == 3) {
                val day = parts[0].toIntOrNull() ?: 1
                val month = parts[1].toIntOrNull() ?: 1
                getSunSign(day, month)
            } else {
                "Koç" // fallback
            }

            if (email.isNotEmpty() && password.length >= 6 && name.isNotEmpty()) {
                val sharedPref = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().apply {
                    putString("email", email)
                    putString("name", name)
                    putString("birthDate", birthDate)
                    putString("sunSign", sunSign)
                    apply()
                }

                Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Lütfen bilgileri eksiksiz girin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
