package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import javax.inject.Inject

class DeleteTeamByUserUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(userId: String) = pokemonRepository.deleteTeamByUser(userId)
}