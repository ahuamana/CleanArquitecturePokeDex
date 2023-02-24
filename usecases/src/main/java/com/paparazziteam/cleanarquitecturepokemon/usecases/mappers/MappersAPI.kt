package com.paparazziteam.cleanarquitecturepokemon.usecases.mappers

import com.paparazziteam.cleanarquitecturepokemon.data.GetPokemonByRegionQuery
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse

fun  List<GetPokemonByRegionQuery.Pokemon>.toPokemonResponse() = map {
    PokemonResponse(
        name = it.name,
        url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${it.order}.png",
        description = it.descripcion_form.filter { description ->
            description.language_form?.language == "es"
        }.first().descripcion,
        tipo = it.details.map { detail ->
            detail.types_form.first().type?.name?:""
        },
        order = it.order?:0
    )
}