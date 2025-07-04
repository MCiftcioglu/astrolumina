package com.upidea.astrolumina.ui.auth



import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.entity.UserEntity
import com.upidea.astrolumina.utils.AstroUtils
import com.upidea.astrolumina.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.TimeZone

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val editName = findViewById<EditText>(R.id.editTextName)
        val editBirthDate = findViewById<EditText>(R.id.editTextBirthDate)
        val editBirthTime = findViewById<EditText>(R.id.editTextBirthTime)
        val editBirthPlace = findViewById<EditText>(R.id.editTextBirthPlace)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radioGroupGender)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        // Automatically insert slashes while typing the birth date (DD/MM/YYYY)
        editBirthDate.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val digits = s.toString().filter { it.isDigit() }
                val builder = StringBuilder()
                for (i in digits.indices) {
                    builder.append(digits[i])
                    if (i == 1 || i == 3) builder.append('/')
                }
                if (builder.length > 10) builder.setLength(10)

                val formatted = builder.toString()
                if (formatted != s.toString()) {
                    s?.replace(0, s.length, formatted)
                }

                isFormatting = false
            }
        })

        // Automatically insert a colon while typing the birth time (HH:MM)
        editBirthTime.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val digits = s.toString().filter { it.isDigit() }
                val builder = StringBuilder()
                for (i in digits.indices) {
                    builder.append(digits[i])
                    if (i == 1) builder.append(':')
                }
                if (builder.length > 5) builder.setLength(5)

                val formatted = builder.toString()
                if (formatted != s.toString()) {
                    s?.replace(0, s.length, formatted)
                }

                isFormatting = false
            }
        })

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
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            val uid = firebaseUser?.uid ?: ""

                            val (sun, moon, rising) = AstroUtils.calculateSignsViaPython(this, birthDate, birthTime, birthPlace)

                            val userToRegister = UserEntity(
                                uid = uid,
                                name = name,
                                email = email,
                                password = password, // Not recommended to store plain password
                                birthDate = birthDate,
                                birthTime = birthTime,
                                birthPlace = birthPlace,
                                gender = gender,
                                sunSign = sun,
                                moonSign = moon,
                                risingSign = rising
                            )
                            registerViewModel.registerUser(userToRegister)

                            Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Kayıt başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Lütfen tüm bilgileri eksiksiz doldurun.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
