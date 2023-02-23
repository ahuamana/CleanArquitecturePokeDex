package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import com.paparazziteam.cleanarquitecturepokemon.domain.RegionResponse
import retrofit2.Response
import retrofit2.http.GET

interface PokemonService {
    @GET("region")
    suspend fun getRegions(): Response<RegionResponse>
}