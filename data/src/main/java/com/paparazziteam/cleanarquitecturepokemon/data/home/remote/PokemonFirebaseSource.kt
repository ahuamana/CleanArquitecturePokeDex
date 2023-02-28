package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import androidx.lifecycle.LiveData
import com.paparazziteam.cleanarquitecturepokemon.domain.GeneralResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import kotlinx.coroutines.flow.Flow
import pe.com.tarjetaw.android.client.shared.network.Resource

interface PokemonFirebaseSource {
    suspend fun createTeam(pokemonTeam:PokemonTeam) : Resource<GeneralResponse>
    suspend fun addPokemonToTeam(teamId: String, pokemon: PokemonResponse)
    suspend fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse)
    suspend fun getTeamsByUser(userId: String): LiveData<Resource<List<PokemonTeam>>>
    suspend fun deleteTeamById(teamId: String): GeneralResponse
    suspend fun deleteTeamByUser(userId: String) : Resource<GeneralResponse>
    suspend fun updatePokemonTeam(teamId: String, pokemonOld:PokemonResponse , pokemon: PokemonResponse) : Resource<GeneralResponse>
    suspend fun getPokemonTeamByToken(token: String): Resource<PokemonTeam>
}