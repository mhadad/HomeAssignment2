package com.mf.homeassignment2.domain.utils

import androidx.recyclerview.widget.DiffUtil
import com.mf.homeassignment2.data.models.CountryUI_DomainModel

class DiffCallback : DiffUtil.ItemCallback<CountryUI_DomainModel>(){
    override fun areItemsTheSame(
        oldItem: CountryUI_DomainModel,
        newItem: CountryUI_DomainModel
    ): Boolean = oldItem.code === newItem.code

    override fun areContentsTheSame(
        oldItem: CountryUI_DomainModel,
        newItem: CountryUI_DomainModel
    ): Boolean = oldItem.code == newItem.code
}