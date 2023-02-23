package com.paparazziteam.cleanarquitecturepokemon.feature.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityHomeBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import pe.com.tarjetaw.android.client.shared.network.Event

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observers()
    }

    private fun observers() {
        viewModel.eventsRegions.observe(this, Observer(::handleEvents))
    }

    private fun handleEvents(event: Event<HomeViewModel.RegionsState>?) {
        event?.getContentIfNotHandled()?.let {
            when(it){
                is HomeViewModel.RegionsState.Error -> {

                }
                HomeViewModel.RegionsState.HideLoading -> {

                }
                HomeViewModel.RegionsState.ShowLoading -> {

                }
                is HomeViewModel.RegionsState.Success -> {
                    println("Success")
                    it.regions.forEach { region ->
                        println(region.name)
                    }
                }
            }
        }
    }
}