
package com.upidea.astrolumina.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.ui.auth.LoginActivity
import com.upidea.astrolumina.ui.OnboardingActivity
import com.upidea.astrolumina.utils.OnboardingPref
import com.upidea.astrolumina.ui.HomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToInitialScreen()
        finish()
    }

    private fun navigateToInitialScreen() {
        val prefs = getSharedPreferences("AstroPrefs", MODE_PRIVATE)

        val targetActivity = if (OnboardingPref(this).isOnboardingSeen()) {
            if (prefs.getBoolean("isLoggedIn", false)) {
                HomeActivity::class.java
            } else {
                LoginActivity::class.java
            }
        } else {
            OnboardingActivity::class.java
        }

        startActivity(Intent(this, targetActivity))
    }
}
