package com.paparazziteam.cleanarquitecturepokemon.data.home.remote


import com.paparazziteam.cleanarquitecturepokemon.domain.LocationResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonLocationResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.RegionResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("region")
    suspend fun getRegions(): Response<RegionResponse>

    @GET("region/{regionId}")
    suspend fun getLocationsByRegion(
        @Path("regionId") regionId:Int,
    ): Response<LocationResponse>

    @GET("location-area/{locationId}")
    suspend fun getPokemonsByLocation(
        @Path("locationId") locationId:Int,
    ): Response<PokemonLocationResponse>


}