package com.paparazziteam.cleanarquitecturepokemon.usecases

import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import com.paparazziteam.cleanarquitecturepokemon.usecases.mappers.toPokemonResponse
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPokemonsByRegionUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    //function to return the first element and Flow<List<PokemonResponse>>
    suspend operator fun invoke(region:String, limit:Int, offset:Int) = pokemonRepository.getPokemonsByRegion(region, limit, offset).map {
        println("GetPokemonsByRegionUseCase: ${it}")
        it?.first()?.pokemons?.toPokemonResponse()?.toMutableList()?: mutableListOf()
    }
}