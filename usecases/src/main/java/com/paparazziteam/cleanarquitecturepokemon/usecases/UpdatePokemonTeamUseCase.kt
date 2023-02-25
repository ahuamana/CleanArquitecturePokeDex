package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import javax.inject.Inject

class UpdatePokemonTeamUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend fun execute(teamId: String, pokemon: PokemonResponse) = pokemonRepository.updatePokemonTeam(teamId, pokemon)
}