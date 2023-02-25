package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonTeamByTokenUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend fun execute(token: String) = pokemonRepository.getPokemonTeamByToken(token)
}