package com.paparazziteam.cleanarquitecturepokemon.data.home.di

import com.apollographql.apollo3.ApolloClient
import com.google.firebase.database.DatabaseReference
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.*
import com.paparazziteam.cleanarquitecturepokemon.data.home.repository.PokemonRepository
import com.paparazziteam.cleanarquitecturepokemon.framework.network.Constans.BASE_URL_GRAPHQL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokemonModule {

    //Service Endpoints
    @Singleton
    @Provides
    fun providePokemonService(retrofit: Retrofit):PokemonService = retrofit.create(PokemonService::class.java)

    //Remote
    @Singleton
    @Provides
    fun providePokemonRemoteDataSource(pokemonService: PokemonService) = PokemonRemoteDataSource(pokemonService)

    //GraphQL
    @Singleton
    @Provides
    fun providePokemonGraphQlDataSource(apolloClient: ApolloClient) = PokemonGraphQlDataSource(apolloClient)

    //firebase
    @Singleton
    @Provides
    fun providePokemonFirebaseDataSource(
        databaseReference: DatabaseReference
    ):PokemonFirebaseSource = PokemonFirebaseSourceImpl(databaseReference)


    //Repository
    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: PokemonRemoteDataSource,
        graphQlDataSource: PokemonGraphQlDataSource,
        pokemonFirebaseSource: PokemonFirebaseSource
    ) = PokemonRepository(remoteDataSource,graphQlDataSource,pokemonFirebaseSource)


}