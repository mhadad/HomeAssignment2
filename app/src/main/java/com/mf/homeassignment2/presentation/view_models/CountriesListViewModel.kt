package com.mf.homeassignment2.presentation.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mf.homeassignment2.data.dataSources.remoteDataSource.REST_API
import com.mf.homeassignment2.data.repos.CountriesRepo
import com.mf.homeassignment2.domain.LoadCountriesUseCase
import com.mf.homeassignment2.domain.models.CountryUI_DomainModel
import com.mf.homeassignment2.domain.models.UIStates
import com.mf.homeassignment2.domain.utils.CountryDomain_UI_Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class CountriesListViewModel(private val baseURL: String): ViewModel(){
    private val _countriesFlow : MutableStateFlow<UIStates<List<CountryUI_DomainModel>?>> =
        MutableStateFlow(UIStates.Success(emptyList()))
    val countriesFlow : StateFlow<UIStates<List<CountryUI_DomainModel>?>> = _countriesFlow
        private val apiService = REST_API(baseURL).getAPIServiceInstance()
    private val countriesRepo = CountriesRepo(apiService)
    private val domainMapper: CountryDomain_UI_Mapper = CountryDomain_UI_Mapper()
    private val loadCountriesUseCase : LoadCountriesUseCase = LoadCountriesUseCase(countriesRepo,domainMapper )
    fun execGetCountries(isInternetAvailable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isInternetAvailable) {
                    setLoading()
                    val countriesResp = loadCountriesUseCase.execGetCountries()
                    countriesResp.catch {
                        setError(it.message.orEmpty())
                    }
                    setData(countriesResp)
                    Log.d("Data", "Raeched")
                } else
                    setError("No internet connection was found")
            } catch (e: Exception) {
                setError(e.message ?: "An Error has occurred")
            }
        }
    }

    private suspend fun setError(message: String){
            _countriesFlow.emit(UIStates.Error(message))
    }
    private suspend fun setData(data:Flow<UIStates<List<CountryUI_DomainModel>?>>){
        data.collect{
            _countriesFlow.value = it
            Log.d("Data", "setData End")
        }
    }
    private suspend fun setLoading(){
            _countriesFlow.emit(UIStates.Loading)
    }

}