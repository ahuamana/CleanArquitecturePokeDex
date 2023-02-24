package com.paparazziteam.cleanarquitecturepokemon.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.data.GetPokemonByRegionQuery
import com.paparazziteam.cleanarquitecturepokemon.domain.*
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.toIntOrZero
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetLocationsByRegionUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetPokemonsByLocationUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetPokemonsByRegionUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetRegionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase,
    private val getPokemonsByRegionUseCase: GetPokemonsByRegionUseCase
) :ViewModel() {

    private val _eventsRegions = MutableLiveData<Event<RegionsState>>()
    val eventsRegions:LiveData<Event<RegionsState>> get() = _eventsRegions

    private val _eventsPokemons = MutableLiveData<Event<PokemonsState>>()
    val eventsPokemons:LiveData<Event<PokemonsState>> get() = _eventsPokemons

    private val regions = mutableListOf<Region>()
    private val pokemons = mutableListOf<PokemonResponse>()
    private var currectRegionSelected:String = ""

    private var offset = 0

    init {
        getRegions()
    }

    private fun getRegions() = getRegionsUseCase().observeForever {
        when(it.status){
            Resource.Status.SUCCESS -> it.run{
                data?.let { response->
                    handleSuccess(response)
                    getPokemonsByRegion(currectRegionSelected)
                }
            }
            Resource.Status.ERROR -> it.run{
                message?.let { message->
                    _eventsRegions.value = Event(RegionsState.HideLoading)
                    _eventsRegions.value = Event(RegionsState.Error(message))
                }
            }
            Resource.Status.LOADING -> {
                _eventsRegions.value = Event(RegionsState.ShowLoading)
            }
        }
    }

    fun getPokemonsByRegion(region:String, limit:Int = 20, offSet:Int=0) = viewModelScope.launch {
        _eventsPokemons.value = Event(PokemonsState.ShowLoading)
        try {
            getPokemonsByRegionUseCase(region, limit, offSet)?.let {
                pokemons.addAll(it)
                _eventsPokemons.value = Event(PokemonsState.HideLoading)
                _eventsPokemons.value = Event(PokemonsState.Success(pokemons))
            }

        }catch (e:Exception){
            _eventsPokemons.value = Event(PokemonsState.Error(e.message.toString()))
            e.printStackTrace()
        }
    }

    fun getPokemonsByRegionNextPage() = viewModelScope.launch {
        offset += 20
        getPokemonsByRegion(currectRegionSelected, 20, offset)
    }

    fun clearOffset() {
        offset = 0
        clearPokemons()
    }

    fun clearPokemons() {
        pokemons.clear()
    }

    private fun handleSuccess(response: RegionResponse) {
        regions.addAll(response.results)
        selectFirstRegion()
        _eventsRegions.value = Event(RegionsState.HideLoading)
        _eventsRegions.value = Event(RegionsState.Success(regions))
    }

    private fun selectFirstRegion() {
        regions.first().isSelected = true
        currectRegionSelected = regions.first().name
    }

    private fun getCurrentIdRegion() = regions.first { it.name == currectRegionSelected }.url.removeSuffix("/").split("/").last().toIntOrZero()

    fun updateRegionSelected(region: Region) {
        regions.forEach {
            it.isSelected = it.name == region.name
        }
        currectRegionSelected = region.name
        _eventsRegions.value = Event(RegionsState.Success(regions))
    }

    sealed class RegionsState {
        object HideLoading : RegionsState()
        object ShowLoading : RegionsState()
        data class Success(val regions: List<Region>) : RegionsState()
        data class Error(val message: String) : RegionsState()
    }

    sealed class PokemonsState{
        object HideLoading : PokemonsState()
        object ShowLoading : PokemonsState()
        data class Success(val pokemons: List<PokemonResponse>) : PokemonsState()
        data class Error(val message: String) : PokemonsState()
    }

}