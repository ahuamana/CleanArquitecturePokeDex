package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import javax.inject.Inject

class CreateTeamUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend fun invoke(pokemonTeam: PokemonTeam) = pokemonRepository.createTeam(pokemonTeam)
}