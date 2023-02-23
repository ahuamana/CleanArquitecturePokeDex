package com.paparazziteam.cleanarquitecturepokemon.feature.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters.RegionAdapter
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityHomeBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import pe.com.tarjetaw.android.client.shared.network.Event

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private var mAdapterRegion = RegionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerViewRegions()
        observers()
    }

    private fun setupRecyclerViewRegions() {
        binding.rvRegions.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapterRegion
        }

        mAdapterRegion.onItemClick { region, position ->
            viewModel.updateRegionSelected(region)
        }
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
                    mAdapterRegion.submitList(it.regions)
                    mAdapterRegion.notifyDataSetChanged()
                }
            }
        }
    }
}