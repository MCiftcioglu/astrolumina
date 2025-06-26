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
            "Doğum Haritanızı Çıkartın",
            "Yıldızların doğduğunuz anki dizilimi, size özel bir kozmik harita sunar. Bu harita, kişiliğinizi, potansiyelinizi ve yaşam yolculuğunuzu anlamanıza yardımcı olur.",
            R.drawable.bg_chart
        ),
        OnboardingItem(
            R.drawable.ic_vedic2,
            "Ay Burcunuzu Öğrenin",
            "Vedik Astroloji, doğduğun anda gökyüzünde oluşan enerjileri rehber alarak içsel dünyanı ve yaşam amacını keşfetmeni sağlar.",
            R.drawable.bg_chart
        ),
        OnboardingItem(
            R.drawable.ic_match2,
            "Kozmik Uyumu Keşfedin",
            "Her ruh bir eş arar. Kozmik uyum, sizinle benzer frekansta olan kişileri bulmanıza yardımcı olur.",
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
                binding.buttonNext.text = if (position == onboardingItems.lastIndex) "Başla" else "İleri"
            }
        })
    }
}
