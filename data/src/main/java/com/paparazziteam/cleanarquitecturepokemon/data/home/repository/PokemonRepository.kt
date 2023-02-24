package com.paparazziteam.cleanarquitecturepokemon.data.home.repository

import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonGraphQlDataSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonRemoteDataSource
import pe.com.tarjetaw.android.client.shared.network.performOnlyNetwork
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonGraphQlDataSource: PokemonGraphQlDataSource
) {
    fun getRegions() =  performOnlyNetwork(
        networkCall = {pokemonRemoteDataSource.getRegions()}
    )

    fun getLocationsByRegion(regionId:Int) =  performOnlyNetwork(
        networkCall = {pokemonRemoteDataSource.getLocationsByRegion(regionId)}
    )

    fun getPokemonsByLocation(locationId:Int) =  performOnlyNetwork(
        networkCall = {pokemonRemoteDataSource.getPokemonsByLocation(locationId)}
    )

    suspend fun getPokemonsByRegion(region:String, limit:Int, offSet:Int) = pokemonGraphQlDataSource.getPokemonsByRegion(region, limit, offSet)
}