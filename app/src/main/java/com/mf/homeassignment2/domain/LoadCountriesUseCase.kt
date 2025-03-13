package com.mf.homeassignment2.domain

import com.mf.homeassignment2.data.models.DataStates
import com.mf.homeassignment2.data.repos.CountriesRepo
import com.mf.homeassignment2.domain.models.CountryUI_DomainModel
import com.mf.homeassignment2.domain.models.UIStates
import com.mf.homeassignment2.domain.utils.CountryDomain_UI_Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class LoadCountriesUseCase(private val countriesRepo: CountriesRepo, private  val domainMapper: CountryDomain_UI_Mapper) {
    suspend fun execGetCountries(): Flow<UIStates.Success<List<CountryUI_DomainModel>?>> {
        return countriesRepo.execGetCountries().transform {
            if(it is DataStates.Success){
                emit(UIStates.Success(domainMapper.submitList(it.data?: emptyList())))
        }
        }
    }
}