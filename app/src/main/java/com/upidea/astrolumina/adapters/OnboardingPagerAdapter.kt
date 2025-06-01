package com.upidea.astrolumina.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.upidea.astrolumina.R
import androidx.recyclerview.widget.RecyclerView
import com.upidea.astrolumina.databinding.ItemOnboardingBinding
import com.upidea.astrolumina.model.OnboardingItem

class OnboardingPagerAdapter(
    private val items: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingItem) {
            binding.imageView.setImageResource(item.imageRes)
            binding.titleText.text = item.title
            binding.subtitleText.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnboardingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
