package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.usecases.DeleteTeamByIdUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.DeleteTeamByUserUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.RemovePokemonFromTeamUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.UpdatePokemonTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class BottomSheetDialogFragmentViewModel @Inject constructor(
    private val removePokemonFromTeamUseCase: RemovePokemonFromTeamUseCase,
    private val deleteTeamByIdUseCase: DeleteTeamByIdUseCase,
    private val updatePokemonTeamUseCase: UpdatePokemonTeamUseCase,
) : ViewModel() {

    private val _eventsDeleteTeamByUser = MutableLiveData<Event<DeleteTeamByUserState>>()
    val eventsDeleteTeamByUser: LiveData<Event<DeleteTeamByUserState>> get() = _eventsDeleteTeamByUser

    private val _eventsUpdatePokemonTeam = MutableLiveData<Event<UpdatePokemonTeamState>>()
    val eventsUpdatePokemonTeam: LiveData<Event<UpdatePokemonTeamState>> get() = _eventsUpdatePokemonTeam

    fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse) = viewModelScope.launch {
        removePokemonFromTeamUseCase(teamId, pokemon)
    }

    fun deleteTeamByUser(userId: String) = viewModelScope.launch {
        deleteTeamByIdUseCase(userId).apply {
            when(this.status){
                Resource.Status.SUCCESS -> {
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.HideLoading)
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.Success(this.data?.message ?: ""))
                }
                Resource.Status.ERROR -> {
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.HideLoading)
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.Error(this.message ?: ""))
                }
                Resource.Status.LOADING -> {
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.ShowLoading)
                }
            }
        }
    }

    fun updatePokemonTeam(teamId: String,pokemonOld:PokemonResponse, pokemon: PokemonResponse) = viewModelScope.launch {
        updatePokemonTeamUseCase(teamId, pokemonOld, pokemon).apply{
            when(this.status){
                Resource.Status.SUCCESS -> {
                    println("updatePokemonTeam -- Sucess")
                    _eventsUpdatePokemonTeam.value = Event(UpdatePokemonTeamState.HideLoading)
                    _eventsUpdatePokemonTeam.value = Event(UpdatePokemonTeamState.Success(this.data?.message ?: ""))
                }
                Resource.Status.ERROR -> {
                    println("updatePokemonTeam -- ${this.message}")
                    _eventsUpdatePokemonTeam.value = Event(UpdatePokemonTeamState.HideLoading)
                    _eventsUpdatePokemonTeam.value = Event(UpdatePokemonTeamState.Error(this.message ?: ""))
                }
                Resource.Status.LOADING -> {
                    _eventsUpdatePokemonTeam.value = Event(UpdatePokemonTeamState.ShowLoading)
                }
            }
        }
    }

    sealed class UpdatePokemonTeamState {
        object ShowLoading : UpdatePokemonTeamState()
        object HideLoading : UpdatePokemonTeamState()
        data class Success(val message: String) : UpdatePokemonTeamState()
        data class Error(val message: String) : UpdatePokemonTeamState()
    }

    sealed class DeleteTeamByUserState {
        object ShowLoading : DeleteTeamByUserState()
        object HideLoading : DeleteTeamByUserState()
        data class Success(val message: String) : DeleteTeamByUserState()
        data class Error(val message: String) : DeleteTeamByUserState()
    }

}