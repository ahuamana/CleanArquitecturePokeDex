package com.paparazziteam.cleanarquitecturepokemon.data.home.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.paparazziteam.cleanarquitecturepokemon.domain.GeneralResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class PokemonFirebaseSourceImpl @Inject constructor(
    private val database: DatabaseReference
) : PokemonFirebaseSource {

    override suspend fun createTeam(pokemonTeam:PokemonTeam): Resource<GeneralResponse> {
        Resource.loading(null)
        val teamRef = database.child("teams").push()
        pokemonTeam.id = teamRef.key.toString()
        return try {
            teamRef.setValue(pokemonTeam).await()
            Resource.success(GeneralResponse(true,"Team created successfully"))
        } catch (e: Exception) {
            Resource.error(e.message?:"", null)
        }
    }

    override suspend fun deleteTeamById(teamId: String): GeneralResponse {
        Resource.loading(null)
        val teamRef = database.child("teams").child(teamId).removeValue().await()
        return GeneralResponse(true,"Team deleted successfully")
    }

    override suspend fun deleteTeamByUser(userId: String): Resource<GeneralResponse> {
        Resource.loading(null)
        val teamRef = database.child("teams")
        val query = teamRef.orderByChild("userId").equalTo(userId)

        val deferred = CompletableDeferred<Resource<GeneralResponse>>()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val childTeam = child.getValue(PokemonTeam::class.java)
                    if (childTeam?.userId == userId) {
                        child.ref.removeValue()
                    }
                }
                deferred.complete(Resource.success(GeneralResponse(true, "Team deleted successfully")))
            }

            override fun onCancelled(error: DatabaseError) {
                deferred.complete(Resource.error(error.message, null))
            }
        })

        return suspendCoroutine { continuation ->
            deferred.invokeOnCompletion {
                val resource = deferred.getCompleted()
                continuation.resumeWith(Result.success(resource))
            }
        }
    }

    override suspend fun updatePokemonTeam(
        teamId: String,
        pokemonOld:PokemonResponse,
        pokemon: PokemonResponse
    ): Resource<GeneralResponse> {
        Resource.loading(null) // event loading
        val teamRef = database.child("teams").child(teamId)
        val query = teamRef.child("pokemon").orderByChild("name").equalTo(pokemonOld.name)

        return suspendCoroutine { continuation ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var foundPokemon  = false
                    snapshot.children.forEach { child ->
                        val childPokemon = child.getValue(PokemonResponse::class.java)
                        if (childPokemon?.name == pokemonOld.name) {
                            child.ref.setValue(pokemon)
                            foundPokemon  = true
                        }
                    }
                    if (foundPokemon) {
                        return continuation.resumeWith(Result.success(Resource.success(GeneralResponse(true,"Team updated successfully"))))
                    } else {
                        return continuation.resumeWith(Result.success(Resource.error("Pokemon not found in team", null)))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.success(Resource.error(error.message, null)))
                }
            })
        }
    }

    override suspend fun getPokemonTeamByToken(token: String): Resource<PokemonTeam> {
        Resource.loading(null) // event loading
        val teamRef = database.child("teams")
        val query = teamRef.orderByChild("metaData/token").equalTo(token)
        return suspendCoroutine { continuation ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(!snapshot.exists() || snapshot.children.count() == 0){
                        return continuation.resumeWith(Result.success(Resource.error("Team not found")))
                    }
                    val team = snapshot.children.first().getValue(PokemonTeam::class.java)
                        ?: return continuation.resumeWith(Result.success(Resource.error("Invalid team data")))

                    continuation.resumeWith(Result.success(Resource.success(team)))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.success(Resource.error(error.message, null)))
                }
            })
        }
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

    override suspend fun getTeamsByUser(userId: String): LiveData<Resource<List<PokemonTeam>>> {
        Resource.loading(null) // event loading
        val teamRef = database.child("teams")
        val query = teamRef.orderByChild("userId").equalTo(userId)
        return object : LiveData<Resource<List<PokemonTeam>>>() {
            override fun onActive() {
                super.onActive()
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val teams = mutableListOf<PokemonTeam>()
                        snapshot.children.forEach { child ->
                            val team = child.getValue(PokemonTeam::class.java)
                            if (team != null) {
                                teams.add(team)
                            }
                        }
                        value = Resource.success(teams)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        value = Resource.error(error.message)
                    }
                })
            }
        }
    }
}