package com.paparazziteam.cleanarquitecturepokemon.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegionResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("previous")
    val previous: String?,
    @SerialName("results")
    val results: List<Region>
)

@Serializable
data class Region(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    var isSelected: Boolean = false // This is for the UI when the user select an item
)


@Serializable
data class GeneralErrorResponse(
    @SerialName("error")
    val error: Error
)

@Serializable
data class Error(
    @SerialName("type")
    val type: String,
    @SerialName("message")
    val message: String
)

@Serializable
data class LocationResponse(
    @SerialName("locations")
    val locations: List<Location>
)

@Serializable
data class Location(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)

@Serializable
data class PokemonLocationResponse(
    @SerialName("pokemon_encounters")
    val pokemon_encounters: List<PokemonResponse>
)

@Serializable
data class PokemonResponse(
    var name: String,
    var url: String,
    var description: String = "",
    var tipo: List<String> = listOf(),
    var order:Int = 0,
    var isSelected:Boolean = false
){
    constructor() : this("", "", "", listOf(), 0, false)
}

data class PokemonTeam(
    var id: String = "",
    var userId: String = "",
    var name: String,
    var pokemon: List<PokemonResponse>,
    var regionName: String = "",
) {
    constructor() : this("","", "", listOf())
}

//GraphQl


