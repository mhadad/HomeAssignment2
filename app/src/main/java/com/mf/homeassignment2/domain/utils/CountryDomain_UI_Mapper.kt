package com.mf.homeassignment2.domain.utils

import com.mf.homeassignment2.data.models.Country_Model
import com.mf.homeassignment2.domain.models.CountryUI_DomainModel

class CountryDomain_UI_Mapper {
    fun submitList(countriesModel: List<Country_Model>): List<CountryUI_DomainModel>{
        return countriesModel.map { mapCountryModel(it) }
    }
    fun mapCountryModel(countryModel: Country_Model): CountryUI_DomainModel{
        return CountryUI_DomainModel(nameRegion = "${countryModel.name},${countryModel.region}", code = countryModel.code, capital = countryModel.capital)
    }
}