package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import javax.inject.Inject

class GetLocationsByRegionUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(regionId:Int) = pokemonRepository.getLocationsByRegion(regionId)
}