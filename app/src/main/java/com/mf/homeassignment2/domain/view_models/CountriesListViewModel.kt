package com.mf.homeassignment2.domain.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mf.homeassignment2.data.models.UI_States
import com.mf.homeassignment2.domain.repository.api.REST_API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.mf.homeassignment2.R
import com.mf.homeassignment2.data.models.CountryUI_DomainModel
import com.mf.homeassignment2.data.models.Country_Model
import com.mf.homeassignment2.domain.utils.NetworkUtils
import com.mf.homeassignment2.domain.utils.CountryDomain_UI_Mapper
import kotlinx.coroutines.sync.Semaphore

//class CountriesListViewModel(private val application: Application, private val countriesRecyclerViewAdapter: CountriesRV_Adapter, private val showLoading: (isActive: Boolean)-> Unit, private val showError: (meesage: String) -> Unit): AndroidViewModel(application = application) {
class CountriesListViewModel(private val application: Application): AndroidViewModel(application = application) {
    private val _countriesFlow : MutableStateFlow<UI_States<List<CountryUI_DomainModel>>> =
        MutableStateFlow(UI_States.Success(emptyList<CountryUI_DomainModel>()))
    var countriesFlow : StateFlow<UI_States<List<CountryUI_DomainModel>>> = _countriesFlow
    private lateinit var baseURL: String
    private val _uiMapper = CountryDomain_UI_Mapper()
    private val apiCallsSemaphore = Semaphore(1)
    init {
        baseURL = application.getString(R.string.base_url)
        execGetCountries()
    }
    fun execGetCountries(){
        try {
            val isInternetAvailable = NetworkUtils.isInternetAvailable(application)
            if (isInternetAvailable) {
                if(apiCallsSemaphore.tryAcquire()) {
                    viewModelScope.launch(Dispatchers.IO) {
                        setLoading(true)
                        val countriesResp = REST_API.getAPIServiceInstance(baseURL).getCountries()
                        if (countriesResp.isSuccessful) {
                            setData(countriesResp.body() ?: emptyList())
                            setLoading(false)
                        } else if (countriesResp.errorBody() != null) {
                            setLoading(false)
                            setError(
                                countriesResp.errorBody()?.string()
                                    ?: application.getString(R.string.network_error_occurred)
                            )
                        }
                    }
                }
                else {
                    throw IllegalStateException(application.getString(R.string.network_error_occurred))
                }
        } else
                setError(application.getString(R.string.no_internet))
        }
        catch (e: Exception){
            setError(e.message ?: "An Error has occurred")
        }
        finally {
            if(apiCallsSemaphore.availablePermits == 0)
                apiCallsSemaphore.release()
        }
    }

    private fun setError(message: String){
        viewModelScope.launch(Dispatchers.IO) {
            _countriesFlow.emit(UI_States.Error(message))
        }
    }
    private fun setData(data:List<Country_Model> ){
        viewModelScope.launch(Dispatchers.Main) {
            _countriesFlow.emit(UI_States.Success(_uiMapper.submitList(data)))
        }
    }
    private fun setLoading(isLoading: Boolean){
        viewModelScope.launch(Dispatchers.Main) {
            _countriesFlow.emit(UI_States.Loading(isLoading = isLoading))
        }
    }

}