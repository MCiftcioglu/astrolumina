package com.upidea.astrolumina

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.ui.login.LoginActivity
import com.upidea.astrolumina.ui.onboarding.OnboardingActivity
import com.upidea.astrolumina.utils.OnboardingPref

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (OnboardingPref(this).isOnboardingSeen()) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, OnboardingActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
