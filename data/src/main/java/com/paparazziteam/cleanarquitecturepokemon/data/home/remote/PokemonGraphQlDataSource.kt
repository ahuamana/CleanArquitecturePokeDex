package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import com.apollographql.apollo3.ApolloClient
import com.paparazziteam.cleanarquitecturepokemon.data.GetPokemonByRegionQuery
import javax.inject.Inject

class PokemonGraphQlDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getPokemonsByRegion(region:String, limit:Int, offSet:Int) = apolloClient.query(GetPokemonByRegionQuery(region, limit, offSet)).execute().data?.pokemon_gen
}