package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

class GetPokemonTeamByTokenUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(token: String) = pokemonRepository.getPokemonTeamByToken(token)
}