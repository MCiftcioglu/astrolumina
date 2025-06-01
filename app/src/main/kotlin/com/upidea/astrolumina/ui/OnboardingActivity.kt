package com.upidea.astrolumina.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.upidea.astrolumina.databinding.ActivityOnboardingBinding
import com.upidea.astrolumina.ui.home.HomeActivity
import com.upidea.astrolumina.ui.onboarding.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingPagerAdapter

    private val onboardingItems = listOf(
        OnboardingItem(
            imageResId = R.drawable.astrolumia_logo,
            title = "AstroLumia’ya Hoş Geldiniz",
            description = "Doğum haritanızla kozmik potansiyelinizi keşfedin."
        ),
        OnboardingItem(
            imageResId = R.drawable.astrolumia_logo,
            title = "Kozmik Bağlantılar",
            description = "Doğum bilgilerinizi girerek ruh eşinizi bulun."
        ),
        OnboardingItem(
            imageResId = R.drawable.astrolumia_logo,
            title = "Vedik Astroloji",
            description = "Premium kullanıcılar için gelişmiş Vedik analizler."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = OnboardingPagerAdapter(onboardingItems)
        binding.viewPager.adapter = adapter

        // dots indicator bağlama
        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dotsIndicator)
        dotsIndicator.attachTo(binding.viewPager)

        // ileri butonu
        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem < onboardingItems.lastIndex) {
                binding.viewPager.currentItem += 1
            } else {
                OnboardingPref(this).setOnboardingSeen()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }


        // ViewPager sayfa değişimiyle buton metnini güncelle
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnNext.text =
                    if (position == onboardingItems.lastIndex) "Başla" else "İleri"
            }
        })
    }
}
