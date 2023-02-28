package com.paparazziteam.cleanarquitecturepokemon.data.home.repository

import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonFirebaseSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonGraphQlDataSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonRemoteDataSource
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import pe.com.tarjetaw.android.client.shared.network.performNetworkFlow
import pe.com.tarjetaw.android.client.shared.network.performNetworkFlowWithoutResource
import pe.com.tarjetaw.android.client.shared.network.performOnlyNetwork
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonGraphQlDataSource: PokemonGraphQlDataSource,
    private val pokemonFirebaseSource: PokemonFirebaseSource
) {
    suspend fun getRegions() =  performNetworkFlowWithoutResource(
        networkCall = {pokemonRemoteDataSource.getRegions()}
    )

    suspend fun getLocationsByRegion(regionId:Int) =  performNetworkFlow(
        networkCall = {pokemonRemoteDataSource.getLocationsByRegion(regionId)}
    )

    suspend fun getPokemonsByLocation(locationId:Int) =  performNetworkFlow(
        networkCall = {pokemonRemoteDataSource.getPokemonsByLocation(locationId)}
    )

    suspend fun getPokemonsByRegion(region:String, limit:Int, offSet:Int) = performNetworkFlowWithoutResource(
        networkCall = {pokemonGraphQlDataSource.getPokemonsByRegion(region, limit, offSet)}
    )


    suspend fun createTeam(pokemonTeam: PokemonTeam) = pokemonFirebaseSource.createTeam(pokemonTeam)

    suspend fun getTeamsByUser(userId: String) = pokemonFirebaseSource.getTeamsByUser(userId)

    suspend fun deleteTeamById(teamId: String) = performNetworkFlowWithoutResource { pokemonFirebaseSource.deleteTeamById(teamId) }

    suspend fun addPokemonToTeam(teamId: String, pokemon: PokemonResponse) = pokemonFirebaseSource.addPokemonToTeam(teamId, pokemon)

    suspend fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse) = pokemonFirebaseSource.removePokemonFromTeam(teamId, pokemon)

    suspend fun deleteTeamByUser(userId: String) = pokemonFirebaseSource.deleteTeamByUser(userId)

    suspend fun updatePokemonTeam(teamId: String,pokemonOld:PokemonResponse, pokemon: PokemonResponse) = pokemonFirebaseSource.updatePokemonTeam(teamId,pokemonOld, pokemon)

    suspend fun getPokemonTeamByToken(token: String) = pokemonFirebaseSource.getPokemonTeamByToken(token)
}