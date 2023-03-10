package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.*
import com.paparazziteam.cleanarquitecturepokemon.usecases.*
import com.paparazziteam.cleanarquitecturepokemon.usecases.mappers.toPokemonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _eventsCreateTeam = MutableLiveData<Event<CreateTeamState>>()
    val eventsCreateTeam:LiveData<Event<CreateTeamState>> get() = _eventsCreateTeam

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

    private fun getRegions() = viewModelScope.launch {
        _eventsRegions.value = Event(RegionsState.ShowLoading)
        getRegionsUseCase
            .invoke()
            .onEach{
                handleSuccess(it) // event success
                getPokemonsByRegion(currectRegionSelected)
            }.catch {
                when(it){
                is InvalidPokemonCreatorException -> _eventsRegions.value = Event(RegionsState.Error(it.message.toString()))
                is Throwable -> _eventsRegions.value = Event(RegionsState.Error(it.message.toString()))
                }
            }.launchIn(viewModelScope)
    }

    fun getPokemonsByRegion(region:String, limit:Int = 20, offSet:Int=0) = viewModelScope.launch {
        _eventsPokemons.value = Event(PokemonsState.ShowLoading)
        getPokemonsByRegionUseCase
            .invoke(region, limit, offSet)
            .onEach {
                _eventsPokemons.value = Event(PokemonsState.HideLoading)
                pokemons.addAll(it)
                _eventsPokemons.value = Event(PokemonsState.Success(pokemons))
            }.catch {
                when(it){
                    is InvalidPokemonCreatorException -> _eventsPokemons.value = Event(PokemonsState.Error(it.message.toString()))
                    is Throwable -> _eventsPokemons.value = Event(PokemonsState.Error(it.message.toString()))
                }
            }.launchIn(viewModelScope)
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

    fun deselectAllPokemons() {
        pokemons.filter { it.isSelected }.forEach {
            it.isSelected = false
        }
        _eventsPokemons.value = Event(PokemonsState.Success(pokemons))
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
            regionName = currectRegionSelected
        )
        viewModelScope.launch {
            createTeamUseCase.invoke(pokemonTeam).apply {
                when(this.status){
                    Resource.Status.SUCCESS -> this.run{
                        data?.let { response->
                            handleSuccessCreateTeam(response)
                        }
                    }
                    Resource.Status.ERROR -> this.run{
                        message?.let { message->
                            _eventsCreateTeam.value = Event(CreateTeamState.HideLoading)
                            _eventsCreateTeam.value = Event(CreateTeamState.Error(message))
                        }
                    }
                    Resource.Status.LOADING -> {
                        _eventsCreateTeam.value = Event(CreateTeamState.ShowLoading)
                    }
                }
            }
        }
    }

    fun copyTeam(pokemonTeam: PokemonTeam, userId: String){
        pokemonTeam.userId = userId
        viewModelScope.launch {
            createTeamUseCase.invoke(pokemonTeam).apply {
                when(this.status){
                    Resource.Status.SUCCESS -> this.run{
                        data?.let { response->
                            handleSuccessCreateTeam(response)
                        }
                    }
                    Resource.Status.ERROR -> this.run{
                        message?.let { message->
                            _eventsCreateTeam.value = Event(CreateTeamState.HideLoading)
                            _eventsCreateTeam.value = Event(CreateTeamState.Error(message))
                        }
                    }
                    Resource.Status.LOADING -> {
                        _eventsCreateTeam.value = Event(CreateTeamState.ShowLoading)
                    }
                }
            }
        }
    }

    private fun handleSuccessCreateTeam(response: GeneralResponse) {
        _eventsCreateTeam.value = Event(CreateTeamState.HideLoading)
        _eventsCreateTeam.value = Event(CreateTeamState.Success(response.message))
        deselectAllPokemons()
    }

    sealed class CreateTeamState{
        object HideLoading : CreateTeamState()
        object ShowLoading : CreateTeamState()
        data class Success(val message: String) : CreateTeamState()
        data class Error(val message: String) : CreateTeamState()
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