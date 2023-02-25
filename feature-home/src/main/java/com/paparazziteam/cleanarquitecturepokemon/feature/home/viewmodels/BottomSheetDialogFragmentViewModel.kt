package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.usecases.DeleteTeamByIdUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.DeleteTeamByUserUseCase
import com.paparazziteam.cleanarquitecturepokemon.usecases.RemovePokemonFromTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class BottomSheetDialogFragmentViewModel @Inject constructor(
    private val removePokemonFromTeamUseCase: RemovePokemonFromTeamUseCase,
    private val deleteTeamByIdUseCase: DeleteTeamByIdUseCase
) : ViewModel() {

    private val _eventsDeleteTeamByUser = MutableLiveData<Event<DeleteTeamByUserState>>()
    val eventsDeleteTeamByUser: LiveData<Event<DeleteTeamByUserState>> get() = _eventsDeleteTeamByUser

    fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse) = viewModelScope.launch {
        removePokemonFromTeamUseCase(teamId, pokemon)
    }

    fun deleteTeamByUser(userId: String) = viewModelScope.launch {
        deleteTeamByIdUseCase(userId).observeForever {
            when(it.status){
                Resource.Status.SUCCESS -> {
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.HideLoading)
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.Success(it.data?.message ?: ""))
                }
                Resource.Status.ERROR -> {
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.HideLoading)
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.Error(it.message ?: ""))
                }
                Resource.Status.LOADING -> {
                    _eventsDeleteTeamByUser.value = Event(DeleteTeamByUserState.ShowLoading)
                }
            }
        }
    }

    sealed class DeleteTeamByUserState {
        object ShowLoading : DeleteTeamByUserState()
        object HideLoading : DeleteTeamByUserState()
        data class Success(val message: String) : DeleteTeamByUserState()
        data class Error(val message: String) : DeleteTeamByUserState()
    }

}