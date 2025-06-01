package com.upidea.astrolumina.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.entity.UserEntity // Added import
import com.upidea.astrolumina.ui.auth.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val editName = findViewById<EditText>(R.id.editTextName)
        val editBirthDate = findViewById<EditText>(R.id.editTextBirthDate)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val name = editName.text.toString().trim()
            val birthDate = editBirthDate.text.toString().trim()
            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            if (email.isNotEmpty() && password.length >= 6 && name.isNotEmpty() && gender.isNotEmpty()) {
                val userToRegister = UserEntity(
                    name = name,
                    email = email,
                    password = password,
                    birthDate = birthDate,
                    birthTime = null,
                    birthPlace = null,
                    gender = gender
                )
                registerViewModel.registerUser(userToRegister)

                Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri eksiksiz doldurun.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
