package com.paparazziteam.cleanarquitecturepokemon.domain

data class RegionResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Region>
)

data class Region(
    val name: String,
    val url: String
)


data class GeneralErrorResponse(
    val error: Error
)