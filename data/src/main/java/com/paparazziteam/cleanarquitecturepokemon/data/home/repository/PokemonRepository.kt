package com.paparazziteam.cleanarquitecturepokemon.data.home.repository

import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonFirebaseSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonGraphQlDataSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonRemoteDataSource
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import pe.com.tarjetaw.android.client.shared.network.performOnlyNetwork
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonGraphQlDataSource: PokemonGraphQlDataSource,
    private val pokemonFirebaseSource: PokemonFirebaseSource
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

    suspend fun createTeam(pokemonTeam: PokemonTeam) = pokemonFirebaseSource.createTeam(pokemonTeam)
}