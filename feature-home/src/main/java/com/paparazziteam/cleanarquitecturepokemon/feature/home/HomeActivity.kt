package com.paparazziteam.cleanarquitecturepokemon.feature.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters.PokemonAdapter
import com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters.RegionAdapter
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityHomeBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import com.paparazziteam.cleanarquitecturepokemon.shared.components.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import pe.com.tarjetaw.android.client.shared.network.Event

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private var mAdapterRegion = RegionAdapter()
    private var mAdapterPokemon = PokemonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerViewRegions()
        setupRecyclerViewPokemons()
        listeners()
        observers()
    }

    private fun listeners() {
        binding.fabCreateTeam.setOnClickListener {
            viewModel.createTeam()
        }
    }

    private fun setupRecyclerViewPokemons() {
        binding.rvPokemons.apply {
            layoutManager =  GridLayoutManager(context, 3)
            adapter = mAdapterPokemon
            addItemDecoration(GridSpacingItemDecoration(3, 20, true))
            addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager?.childCount ?: 0
                    val totalItemCount = layoutManager?.itemCount ?: 0
                    val firstVisibleItemPosition = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= 20
                    ) {
                        viewModel.getPokemonsByRegionNextPage()
                    }
                }
            })
        }

        mAdapterPokemon.onItemClick { pokemon, position ->
            handlePokemonSelected(pokemon, position)
        }
    }

    private fun handlePokemonSelected(selectedPokemon: PokemonResponse, selectedPosition: Int) {
        if(isLimitReached() && !selectedPokemon.isSelected){
            return
        }

        if(isLimitReached() && selectedPokemon.isSelected){
            viewModel.removePokemonSelected(selectedPokemon)
            updateAdapterSelection(selectedPosition)
            return
        }

        togglePokemonSelection(selectedPokemon)
        updateAdapterSelection(selectedPosition)
    }

    private fun updateAdapterSelection(position: Int) {
        mAdapterPokemon.selectItem(position)
    }

    private fun togglePokemonSelection(pokemon: PokemonResponse) {
        if (pokemon.isSelected) {
            viewModel.removePokemonSelected(pokemon)
        } else {
            viewModel.addPokemonSelected(pokemon)
        }
    }


    private fun isLimitReached(): Boolean {
        val selectedPokemons = viewModel.getPokemonsSelected()
        val limit = viewModel.getLimitPokemons()
        return selectedPokemons.size >= limit.last
    }

    private fun setupRecyclerViewRegions() {
        binding.rvRegions.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapterRegion
        }

        mAdapterRegion.onItemClick { region, position ->
            viewModel.updateRegionSelected(region)
            viewModel.clearOffset() // also, clear pokemons selected
            viewModel.getPokemonsByRegion(region.name)
        }
    }

    private fun observers() {
        viewModel.eventsRegions.observe(this, Observer(::handleEvents))
        viewModel.eventsPokemons.observe(this, Observer(::handleEventsPokemons))
        viewModel.error.observe(this, Observer(::handleError))
    }

    private fun handleError(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    private fun handleEventsPokemons(event: Event<HomeViewModel.PokemonsState>?) {
        event?.getContentIfNotHandled()?.let {
            when(it){
                is HomeViewModel.PokemonsState.Error -> {

                }
                HomeViewModel.PokemonsState.HideLoading -> {

                }
                HomeViewModel.PokemonsState.ShowLoading -> {

                }
                is HomeViewModel.PokemonsState.Success -> it.run {
                    mAdapterPokemon.submitList(pokemons)
                    mAdapterPokemon.notifyDataSetChanged()
                }
            }
        }
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