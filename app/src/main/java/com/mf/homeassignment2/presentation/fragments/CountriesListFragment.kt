package com.mf.homeassignment2.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.mf.homeassignment2.R
import com.mf.homeassignment2.databinding.CountriesListBinding
import com.mf.homeassignment2.domain.models.CountryUI_DomainModel
import com.mf.homeassignment2.domain.models.UIStates
import com.mf.homeassignment2.domain.utils.NetworkUtils
import com.mf.homeassignment2.presentation.adapters.CountriesRV_Adapter
import com.mf.homeassignment2.presentation.view_models.CountriesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CountriesListFragment : Fragment(R.layout.countries_list) {
    private lateinit var _binding : CountriesListBinding
    private lateinit var _countriesListViewModel: CountriesListViewModel
    private lateinit var _countriesRV_Adapter: CountriesRV_Adapter
    private var isInternetAvailable : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CountriesListBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
            _countriesRV_Adapter = CountriesRV_Adapter()
            _countriesListViewModel =
                CountriesListViewModel(baseURL = getString(R.string.base_url), )
        }
        loadData()
    }
    private fun loadData(){
        var ctxt = context?.apply {
            isInternetAvailable = NetworkUtils().isInternetAvailable(this)
        }
        if(ctxt != null)
            _countriesListViewModel.execGetCountries(isInternetAvailable = isInternetAvailable)
        else
            setError(getString(R.string.no_internet))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.let {
                it.countriesRV.layoutManager = LinearLayoutManager(requireContext())
                it.countriesRV.adapter = _countriesRV_Adapter
                it.countriesRV.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
//        lifecycleScope.launch{
//            _countriesListViewModel.countriesFlow.flowWithLifecycle(lifecycle).collectLatest { state ->
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                _countriesListViewModel.countriesFlow.collectLatest { state ->
                    Log.d("Data", "${state.javaClass.simpleName}")
                    when (state) {
                        is UIStates.Success -> {
                            setLoading(false)
                            setData(state.data)
                        }
                        is UIStates.Error -> {
                            setLoading(false)
                            setError(state.message)
                        }
                        is UIStates.Loading -> setLoading()
                    }
                }
            }
//                }
        }
        _binding.countriesListSRL.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                loadData()
            }

        })
    }


    private fun setError(message: String){
        lifecycleScope.launch(Dispatchers.Main) {
            _binding.countriesListSRL.isRefreshing = false
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setLoading(isActive: Boolean = true){
        lifecycleScope.launch(Dispatchers.Main) {
            _binding.countriesListSRL.isRefreshing = false
            if (!isActive) {
                _binding.progressBar.visibility = View.GONE
            } else {
                val pbToggleVal = _binding.progressBar.visibility
                if (pbToggleVal == View.GONE)
                    _binding.progressBar.visibility = View.VISIBLE
                else if (pbToggleVal == View.VISIBLE)
                    _binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setData(data : List<CountryUI_DomainModel?>?) {
        lifecycleScope.launch(Dispatchers.Main) {
            data?.let {
                if (it.count() > 0) {
                    _binding.countriesListSRL.isRefreshing = false
                    _countriesRV_Adapter.submitList(data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(_binding != null) {
            setLoading(false)
        }
    }
}