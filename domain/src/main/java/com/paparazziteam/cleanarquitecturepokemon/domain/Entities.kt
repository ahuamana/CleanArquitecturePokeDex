package com.paparazziteam.cleanarquitecturepokemon.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class GeneralResponse(
    val status: Boolean,
    val message: String
)
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
) {
    constructor() : this(0, "", "", listOf())
}

@Serializable
data class Region(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
    var isSelected: Boolean = false // This is for the UI when the user select an item
){
    constructor() : this("", "", false)
}


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
    var pokemon: List<PokemonResponse> ? = null,
    var regionName: String = "",
    var metaData: MetaData = MetaData()
) {
    constructor() : this("","", "", listOf())
}

data class MetaData(
    var token: String = ""
) {
    constructor() : this("")
}

@Parcelize
data class PokemonTeamParcelable(
    var id: String = "",
    var userId: String = "",
    var name: String,
    var pokemon: List<PokemonResponseParcelable> ? = null,
    var regionName: String = "",
    val metaData: MetadataParcelable = MetadataParcelable()
):Parcelable

@Parcelize
data class PokemonResponseParcelable(
    var name: String,
    var url: String,
    var description: String = "",
    var tipo: List<String> = listOf(),
    var order:Int = 0,
    var isSelected:Boolean = false
):Parcelable

@Parcelize
data class MetadataParcelable(
    var token: String = ""
):Parcelable


fun PokemonTeam.toParcelable() = PokemonTeamParcelable(
    id = this.id,
    userId = this.userId,
    name = this.name,
    pokemon = this.pokemon?.map { it.toParcelable() },
    regionName = this.regionName,
    metaData = this.metaData.toParcelable()
)

fun PokemonTeamParcelable.toDomain() = PokemonTeam(
    id = this.id,
    userId = this.userId,
    name = this.name,
    pokemon = this.pokemon?.map { it.toDomain() },
    regionName = this.regionName,
    metaData = this.metaData.toDomain()
)

fun PokemonResponse.toParcelable() = PokemonResponseParcelable(
    name = this.name,
    url = this.url,
    description = this.description,
    tipo = this.tipo,
    order = this.order,
    isSelected = this.isSelected
)

fun PokemonResponseParcelable.toDomain() = PokemonResponse(
    name = this.name,
    url = this.url,
    description = this.description,
    tipo = this.tipo,
    order = this.order,
    isSelected = this.isSelected
)

fun MetadataParcelable.toDomain() = MetaData(
    token = this.token
)

fun MetaData.toParcelable() = MetadataParcelable(
    token = this.token
)


