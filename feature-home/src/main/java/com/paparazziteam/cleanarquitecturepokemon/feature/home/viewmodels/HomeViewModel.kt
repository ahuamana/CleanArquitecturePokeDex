package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.*
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.generateToken
import com.paparazziteam.cleanarquitecturepokemon.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase,
    private val getPokemonsByRegionUseCase: GetPokemonsByRegionUseCase,
    private val createTeamUseCase: CreateTeamUseCase
) :ViewModel() {

    private val _eventsRegions = MutableLiveData<Event<RegionsState>>()
    val eventsRegions:LiveData<Event<RegionsState>> get() = _eventsRegions

    private val _eventsPokemons = MutableLiveData<Event<PokemonsState>>()
    val eventsPokemons:LiveData<Event<PokemonsState>> get() = _eventsPokemons

    private val _error = MutableLiveData<String>()
    val error:LiveData<String> get() = _error

    private val regions = mutableListOf<Region>()
    private val pokemons = mutableListOf<PokemonResponse>()
    private var currectRegionSelected:String = ""

    private var limitPokemons = 3..6
    private var pokemonsAvailableSelected = mutableListOf<PokemonResponse>()

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
        clearPokemonsSelected()
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

    fun addPokemonSelected(pokemon: PokemonResponse) {
        if(pokemonsAvailableSelected.size < limitPokemons.last) {
            pokemonsAvailableSelected.add(pokemon)
        }
    }

    fun removePokemonSelected(pokemon: PokemonResponse) {
        pokemonsAvailableSelected.remove(pokemon)
    }

    fun clearPokemonsSelected() {
        pokemonsAvailableSelected.clear()
    }

    fun getPokemonsSelected() = pokemonsAvailableSelected

    fun getLimitPokemons() = limitPokemons

    fun updateRegionSelected(region: Region) {
        regions.forEach {
            it.isSelected = it.name == region.name
        }
        currectRegionSelected = region.name
        _eventsRegions.value = Event(RegionsState.Success(regions))
    }


    fun isAvailableToCreateTeam(): Boolean {
        if(pokemonsAvailableSelected.size < limitPokemons.first) {
            _error.value = "You must select ${limitPokemons.first} pokemons at least"
            return false
        }

        if(pokemonsAvailableSelected.size > limitPokemons.last) {
            _error.value = "You must select ${limitPokemons.last} pokemons at most"
            return false
        }
        return true
    }

    fun createTeamFirebase(email:String, name:String){
        val pokemonTeam = PokemonTeam(
            name = name,
            pokemon = getPokemonsSelected(),
            userId = email,
            regionName = currectRegionSelected,
            token = generateToken()
        )
        viewModelScope.launch {
            createTeamUseCase.invoke(pokemonTeam)
        }
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