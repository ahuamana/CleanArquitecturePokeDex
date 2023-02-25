package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonFirebaseSource
import javax.inject.Inject

class GetTeamsByUserUseCase @Inject constructor(
    private val pokemonFirebaseSource: PokemonFirebaseSource
) {
    suspend fun invoke(userId: String) = pokemonFirebaseSource.getTeamsByUser(userId)
}