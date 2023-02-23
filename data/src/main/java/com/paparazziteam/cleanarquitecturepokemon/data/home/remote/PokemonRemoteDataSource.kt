package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import com.paparazziteam.cleanarquitecturepokemon.framework.network.BaseDataSource
import javax.inject.Inject

class PokemonRemoteDataSource @Inject constructor(
    private val pokemonService: PokemonService
):BaseDataSource() {
    suspend fun getRegions() = getResult { pokemonService.getRegions() }

}