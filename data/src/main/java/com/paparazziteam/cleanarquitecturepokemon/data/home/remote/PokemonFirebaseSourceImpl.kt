package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PokemonFirebaseSourceImpl @Inject constructor(
    private val database: DatabaseReference
) : PokemonFirebaseSource {

    override suspend fun createTeam(pokemonTeam:PokemonTeam) {
        val teamId = database.child("teams").push().key ?: throw Exception("Failed to create team")
        database.child("teams").child(teamId).setValue(pokemonTeam).await()
    }

    override suspend fun addPokemonToTeam(teamId: String, pokemon: PokemonResponse) {
        val teamRef = database.child("teams").child(teamId)
        teamRef.child("pokemon").push().setValue(pokemon).await()
    }

    override suspend fun removePokemonFromTeam(teamId: String, pokemon: PokemonResponse) {
        val teamRef = database.child("teams").child(teamId)
        val query = teamRef.child("pokemon").orderByChild("name").equalTo(pokemon.name)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val childPokemon = child.getValue(PokemonResponse::class.java)
                    if (childPokemon?.name == pokemon.name) {
                        child.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


}