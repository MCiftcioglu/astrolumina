package com.upidea.astrolumina.ui.auth

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.entity.UserEntity
import com.upidea.astrolumina.utils.AstroUtils
import com.upidea.astrolumina.viewmodel.RegisterViewModel
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
        val editBirthTime = findViewById<EditText>(R.id.editTextBirthTime)
        val editBirthPlace = findViewById<EditText>(R.id.editTextBirthPlace)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val name = editName.text.toString().trim()
            val birthDate = editBirthDate.text.toString().trim()
            val birthTime = editBirthTime.text.toString().trim()
            val birthPlace = editBirthPlace.text.toString().trim()
            val selectedGenderId = radioGroupGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            if (email.isNotEmpty() && password.length >= 6 && name.isNotEmpty() &&
                gender.isNotEmpty() && birthDate.isNotEmpty() && birthTime.isNotEmpty() && birthPlace.isNotEmpty()
            ) {

                val (sun, moon, rising) = AstroUtils.calculateSignsViaPython(this, birthDate, birthTime, birthPlace)

                val userToRegister = UserEntity(
                    name = name,
                    email = email,
                    password = password,
                    birthDate = birthDate,
                    birthTime = birthTime,
                    birthPlace = birthPlace,
                    gender = gender,
                    sunSign = sun
                )
                registerViewModel.registerUser(userToRegister)

                val prefs = getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString("name", name)
                    putString("birthDate", birthDate)
                    putString("birthTime", birthTime)
                    putString("birthPlace", birthPlace)
                    putString("gender", gender)
                    putString("sunSign", sun)
                    putString("moonSign", moon)
                    putString("risingSign", rising)
                    apply()
                }

                Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri eksiksiz doldurun.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
