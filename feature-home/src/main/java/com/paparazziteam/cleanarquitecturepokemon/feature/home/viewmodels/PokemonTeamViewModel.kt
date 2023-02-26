package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.filterNotNullItems
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetPokemonTeamByTokenUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetTeamsByUserUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.UpdatePokemonTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class PokemonTeamViewModel @Inject constructor(
    private val getTeamsByUserUseCase: GetTeamsByUserUseCase,
    private val getPokemonTeamByTokenUseCase: GetPokemonTeamByTokenUseCase
):ViewModel() {

    private val _events =  MutableLiveData<Event<PokemonTeamEvent>>()
    val events : LiveData<Event<PokemonTeamEvent>> get() = _events

    private val _eventsByToken =  MutableLiveData<Event<PokemonTeamByTokenEvent>>()
    val eventsByToken : LiveData<Event<PokemonTeamByTokenEvent>> get() = _eventsByToken

    fun getTeamsByUser(userId: String) = viewModelScope.launch {
        getTeamsByUserUseCase.invoke(userId).observeForever {
            when(it.status){
                Resource.Status.SUCCESS -> it.run{
                    //filter pokemons that are inside team that are not null
                    it.data?.forEach { team ->
                        team.pokemon = team.pokemon?.filterNotNullItems()
                    }

                    _events.value = Event(PokemonTeamEvent.HideLoading)
                    _events.value = Event(PokemonTeamEvent.Success(it.data?: listOf()))
                }
                Resource.Status.ERROR -> {
                    _events.value = Event(PokemonTeamEvent.HideLoading)
                    _events.value = Event(PokemonTeamEvent.Error(it.message?:""))
                }
                Resource.Status.LOADING -> {
                    _events.value = Event(PokemonTeamEvent.ShowLoading)
                }
            }
        }
    }

    fun getPokemonTeamByToken(token:String) = viewModelScope.launch {
        getPokemonTeamByTokenUseCase.invoke(token).apply {
            when(this.status){
                Resource.Status.SUCCESS -> {
                    this.data?.let {
                        _eventsByToken.value = Event(PokemonTeamByTokenEvent.HideLoading)
                        _eventsByToken.value = Event(PokemonTeamByTokenEvent.Success(it))
                    }
                }
                Resource.Status.ERROR -> {
                    _eventsByToken.value = Event(PokemonTeamByTokenEvent.HideLoading)
                    _eventsByToken.value = Event(PokemonTeamByTokenEvent.Error(this.message?:""))
                }
                Resource.Status.LOADING -> {
                    _eventsByToken.value = Event(PokemonTeamByTokenEvent.ShowLoading)
                }
            }
        }
    }

    sealed class PokemonTeamByTokenEvent {
        data class Success(val pokemonTeam: PokemonTeam) : PokemonTeamByTokenEvent()
        data class Error(val error: String) : PokemonTeamByTokenEvent()
        object ShowLoading : PokemonTeamByTokenEvent()
        object HideLoading : PokemonTeamByTokenEvent()
    }

    sealed class PokemonTeamEvent {
        data class Success(val pokemonTeam: List<PokemonTeam>) : PokemonTeamEvent()
        data class Error(val error: String) : PokemonTeamEvent()
        object ShowLoading : PokemonTeamEvent()
        object HideLoading : PokemonTeamEvent()
    }

    override fun onCleared() {
        super.onCleared()
        removeObservers()
    }

    private fun removeObservers() {

    }

}