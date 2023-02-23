package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import javax.inject.Inject

class PokemonRemoteDataSource @Inject constructor(
    private val pokemonService: PokemonService
) {
}