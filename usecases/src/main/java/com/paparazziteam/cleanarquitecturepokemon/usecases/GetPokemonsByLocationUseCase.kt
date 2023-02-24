package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonsByLocationUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(locationId:Int) = pokemonRepository.getPokemonsByLocation(locationId)
}