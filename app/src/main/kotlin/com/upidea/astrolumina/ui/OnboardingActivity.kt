package com.upidea.astrolumina.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.upidea.astrolumina.R
import com.upidea.astrolumina.adapters.OnboardingPagerAdapter
import com.upidea.astrolumina.databinding.ActivityOnboardingBinding
import com.upidea.astrolumina.model.OnboardingItem
import com.upidea.astrolumina.ui.auth.LoginActivity
import com.upidea.astrolumina.utils.OnboardingPref

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingPagerAdapter

    private val onboardingItems = listOf(
        OnboardingItem(
            R.drawable.ic_birthmap2,
            "Generate Your Birth Chart",
            "The arrangement of the stars at your birth forms a unique cosmic map. This chart helps you understand your personality, potential, and life journey.",
            R.drawable.bg_chart
        ),
        OnboardingItem(
            R.drawable.ic_vedic2,
            "Discover Your Moon Sign",
            "Vedic Astrology uses the energies present at your birth to explore your inner world and life purpose.",
            R.drawable.bg_chart
        ),
        OnboardingItem(
            R.drawable.ic_match2,
            "Explore Cosmic Compatibility",
            "Every soul seeks a match. Cosmic compatibility helps you connect with people on a similar wavelength.",
            R.drawable.bg_chart
        )

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewFlipper için fade animasyonları
        binding.imageFlipper.apply {
            inAnimation = AnimationUtils.loadAnimation(this@OnboardingActivity, android.R.anim.fade_in)
            outAnimation = AnimationUtils.loadAnimation(this@OnboardingActivity, android.R.anim.fade_out)
            startFlipping()
        }

        // Onboarding ViewPager ayarları
        adapter = OnboardingPagerAdapter(onboardingItems)
        binding.viewPagerOnboarding.adapter = adapter

        // DotsIndicator bağlama
        binding.dotsIndicator.attachTo(binding.viewPagerOnboarding)

        // İleri/Başla butonu
        binding.buttonNext.setOnClickListener {
            if (binding.viewPagerOnboarding.currentItem < onboardingItems.lastIndex) {
                binding.viewPagerOnboarding.currentItem += 1
            } else {
                OnboardingPref(this).setOnboardingSeen()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        // Sayfa değişimine göre buton metnini güncelle
        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.buttonNext.text = if (position == onboardingItems.lastIndex) "Start" else "Next"
            }
        })
    }
}
