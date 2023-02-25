package com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.usecases.RemovePokemonFromTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSheetDialogFragmentViewModel @Inject constructor(
    private val removePokemonFromTeamUseCase: RemovePokemonFromTeamUseCase
) : ViewModel() {

    fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse) = viewModelScope.launch {
        removePokemonFromTeamUseCase(teamId, pokemon)
    }

}