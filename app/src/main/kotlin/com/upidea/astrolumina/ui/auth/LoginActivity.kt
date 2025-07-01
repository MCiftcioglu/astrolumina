package com.upidea.astrolumina.ui.auth

import java.util.TimeZone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.upidea.astrolumina.databinding.ActivityLoginBinding
import com.upidea.astrolumina.ui.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            firebaseUser?.let { user ->
                                firestore.collection("users").document(user.uid).get()
                                    .addOnSuccessListener { document ->
                                        if (document != null && document.exists()) {
                                            val userEntity = document.toObject(com.upidea.astrolumina.data.local.entity.UserEntity::class.java)
                                            val sharedPref = getSharedPreferences("AstroPrefs", MODE_PRIVATE)
                                            sharedPref.edit().apply {
                                                putBoolean("isLoggedIn", true)
                                                putString("uid", userEntity?.uid)
                                                putString("name", userEntity?.name)
                                                putString("birthDate", userEntity?.birthDate)
                                                putString("birthTime", userEntity?.birthTime)
                                                putString("birthPlace", userEntity?.birthPlace)
                                                putString("gender", userEntity?.gender)
                                                putString("sunSign", userEntity?.sunSign)
                                                putString("moonSign", userEntity?.moonSign)
                                                putString("risingSign", userEntity?.risingSign)
                                                apply()
                                            }
                                            startActivity(Intent(this, HomeActivity::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Kullanıcı verisi bulunamadı.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Veri alınamadı: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Giriş başarısız: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "E-posta ve şifre boş olamaz.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
