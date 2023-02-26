package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import androidx.lifecycle.LiveData
import com.paparazziteam.cleanarquitecturepokemon.domain.GeneralResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import pe.com.tarjetaw.android.client.shared.network.Resource

interface PokemonFirebaseSource {
    suspend fun createTeam(pokemonTeam:PokemonTeam) : LiveData<Resource<GeneralResponse>>
    suspend fun addPokemonToTeam(teamId: String, pokemon: PokemonResponse)
    suspend fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse)
    suspend fun getTeamsByUser(userId: String): LiveData<Resource<List<PokemonTeam>>>
    suspend fun deleteTeamById(teamId: String): LiveData<Resource<GeneralResponse>>
    suspend fun deleteTeamByUser(userId: String) : LiveData<Resource<GeneralResponse>>

    suspend fun updatePokemonTeam(teamId: String, pokemonOld:PokemonResponse , pokemon: PokemonResponse) : LiveData<Resource<GeneralResponse>>

    suspend fun getPokemonTeamByToken(token: String): LiveData<Resource<PokemonTeam>>
}