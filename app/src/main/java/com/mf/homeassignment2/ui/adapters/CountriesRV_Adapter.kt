package com.mf.homeassignment2.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mf.homeassignment2.data.models.CountryUI_DomainModel
import com.mf.homeassignment2.databinding.CountryItemViewBinding
import com.mf.homeassignment2.domain.utils.DiffCallback

class CountriesRV_Adapter() : ListAdapter<CountryUI_DomainModel, CountriesRV_Adapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(CountryItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false ))

    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: CountryItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(countryUIModel: CountryUI_DomainModel){
            binding.nameRegionTV.text = countryUIModel.nameRegion
            binding.codeTV.text = countryUIModel.code
            binding.capitalTV.text = countryUIModel.capital
        }
    }
}