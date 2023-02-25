package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import javax.inject.Inject

class DeleteTeamByIdUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(teamId: String) = pokemonRepository.deleteTeamById(teamId)
}