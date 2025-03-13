package com.mf.homeassignment2



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mf.homeassignment2.databinding.MainActivityBinding
import com.mf.homeassignment2.presentation.fragments.CountriesListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = DataBindingUtil.inflate<MainActivityBinding>(layoutInflater, R.layout.main_activity,null, false)
        setContentView(mainActivityBinding.root)
        if(savedInstanceState == null) {
            showCountriesListFragment()
        }
    }
    fun showCountriesListFragment(){
        val countriesListFragment : Fragment = CountriesListFragment()
        supportFragmentManager.beginTransaction().replace(mainActivityBinding.contentLayout.id,  countriesListFragment).commit()
    }
}