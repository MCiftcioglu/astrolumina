package com.upidea.astrolumina.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Fragment daha önce eklenmediyse yükle
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}
