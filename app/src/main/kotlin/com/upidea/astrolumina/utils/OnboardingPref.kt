package com.upidea.astrolumina.utils

import android.content.Context
import android.content.SharedPreferences

class OnboardingPref(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    fun setOnboardingSeen() {
        prefs.edit().putBoolean("seen", true).apply()
    }

    fun isOnboardingSeen(): Boolean {
        return prefs.getBoolean("seen", false)
    }
}
