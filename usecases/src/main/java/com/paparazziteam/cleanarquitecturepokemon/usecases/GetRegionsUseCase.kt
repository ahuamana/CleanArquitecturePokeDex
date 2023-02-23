package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import javax.inject.Inject

class GetRegionsUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke() = pokemonRepository.getRegions()
}