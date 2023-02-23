package com.paparazziteam.cleanarquitecturepokemon.data.home.repository

import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonRemoteDataSource
import pe.com.tarjetaw.android.client.shared.network.performOnlyNetwork
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource
) {
    fun getRegions() =  performOnlyNetwork(
        networkCall = {pokemonRemoteDataSource.getRegions()}
    )
}