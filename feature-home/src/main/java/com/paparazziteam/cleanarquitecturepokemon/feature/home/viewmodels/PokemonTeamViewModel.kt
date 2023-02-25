package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetTeamsByUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class PokemonTeamViewModel @Inject constructor(
    private val getTeamsByUserUseCase: GetTeamsByUserUseCase
):ViewModel() {

    private val _events =  MutableLiveData <Event<PokemonTeamEvent>>()
    val events : LiveData<Event<PokemonTeamEvent>> get() = _events
    fun getTeamsByUser(userId: String) = viewModelScope.launch {
        getTeamsByUserUseCase.invoke(userId).observeForever {
            when(it.status){
                Resource.Status.SUCCESS -> it.run{
                    _events.value = Event(PokemonTeamEvent.HideLoading)
                    _events.value = Event(PokemonTeamEvent.Success(data!!))
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

    sealed class PokemonTeamEvent {
        data class Success(val pokemonTeam: List<PokemonTeam>) : PokemonTeamEvent()
        data class Error(val error: String) : PokemonTeamEvent()
        object ShowLoading : PokemonTeamEvent()
        object HideLoading : PokemonTeamEvent()
    }

}