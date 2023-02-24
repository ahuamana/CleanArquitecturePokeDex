package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam

interface PokemonFirebaseSource {
    suspend fun createTeam(pokemonTeam:PokemonTeam)
    suspend fun addPokemonToTeam(teamId: String, pokemon: PokemonResponse)
    suspend fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse)
}