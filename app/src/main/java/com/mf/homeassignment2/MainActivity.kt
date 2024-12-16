package com.mf.homeassignment2



import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mf.homeassignment2.databinding.MainActivityBinding
import com.mf.homeassignment2.ui.fragments.CountriesListFragment

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