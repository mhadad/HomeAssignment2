package com.mf.homeassignment2.ui.view_models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mf.homeassignment2.data.models.UI_States
import com.mf.homeassignment2.data.dataSources.remoteDataSource.REST_API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.mf.homeassignment2.R
import com.mf.homeassignment2.data.models.CountryUI_DomainModel
import com.mf.homeassignment2.data.models.Country_Model
import com.mf.homeassignment2.domain.utils.NetworkUtils
import com.mf.homeassignment2.domain.utils.CountryDomain_UI_Mapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Semaphore
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class CountriesListViewModel(private val baseURL: String): ViewModel(){
    private val _countriesFlow : MutableStateFlow<UI_States<List<CountryUI_DomainModel>>> =
        MutableStateFlow(UI_States.Success(emptyList<CountryUI_DomainModel>()))
    var countriesFlow : StateFlow<UI_States<List<CountryUI_DomainModel>>> = _countriesFlow
    private val _uiMapper = CountryDomain_UI_Mapper()
    fun execGetCountries(isInternetAvailable: Boolean, coroutineContext : CoroutineContext = viewModelScope.coroutineContext){
        try {
            if (isInternetAvailable) {
                viewModelScope.launch(Dispatchers.IO) {
                    setLoading(true)
                    val countriesResp = REST_API(baseURL).getAPIServiceInstance().getCountries()
                    if (countriesResp.isSuccessful) {
                        setData(countriesResp.body() ?: emptyList())
                        setLoading(false)
                    } else if (countriesResp.errorBody() != null) {
                        setLoading(false)
                        setError(
                            countriesResp.errorBody()?.string()
                                ?: "Network error has occurred"
                        )
                    } else {
                        throw IllegalStateException("Network error has occurred")
                    }
                }
        } else
                setError("No internet connection was found")
        }
        catch (e: Exception){
            setError(e.message ?: "An Error has occurred")
        }
    }

    private fun setError(message: String){
        viewModelScope.launch(Dispatchers.IO) {
            _countriesFlow.emit(UI_States.Error(message))
        }
    }
    private fun setData(data:List<Country_Model> ){
        viewModelScope.launch(Dispatchers.IO) {
            _countriesFlow.emit(UI_States.Success(_uiMapper.submitList(data)))
        }
    }
    private fun setLoading(isLoading: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            _countriesFlow.emit(UI_States.Loading(isLoading = isLoading))
        }
    }

}