package com.upidea.astrolumina

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.ui.auth.LoginActivity
import com.upidea.astrolumina.ui.OnboardingActivity
import com.upidea.astrolumina.utils.OnboardingPref

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToInitialScreen()
        finish()
    }

    private fun navigateToInitialScreen() {
        val targetActivity = if (OnboardingPref(this).isOnboardingSeen()) {
            LoginActivity::class.java
        } else {
            OnboardingActivity::class.java
        }

        startActivity(Intent(this, targetActivity))
    }
}
