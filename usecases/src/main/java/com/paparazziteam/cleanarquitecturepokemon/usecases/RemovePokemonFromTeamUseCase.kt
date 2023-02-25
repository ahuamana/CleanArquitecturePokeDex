package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import javax.inject.Inject

class RemovePokemonFromTeamUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(teamId: String, pokemon: PokemonResponse) = pokemonRepository.removePokemonFromTeam(teamId, pokemon)
}