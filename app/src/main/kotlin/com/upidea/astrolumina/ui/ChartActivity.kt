package com.upidea.astrolumina.ui.chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.upidea.astrolumina.R
import com.upidea.astrolumina.ui.ChartFragment

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.chartFragmentContainer, ChartFragment())
                .commit()
        }
    }
}
