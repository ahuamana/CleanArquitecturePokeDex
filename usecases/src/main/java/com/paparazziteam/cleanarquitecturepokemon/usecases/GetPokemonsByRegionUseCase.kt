package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import com.paparazziteam.cleanarquitecturepokemon.usecases.mappers.toPokemonResponse
import javax.inject.Inject

class GetPokemonsByRegionUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
   suspend operator fun invoke(region:String, limit:Int = 20, offSet:Int = 0) = pokemonRepository.getPokemonsByRegion(region, limit, offSet)?.first()?.pokemons?.toPokemonResponse()
}