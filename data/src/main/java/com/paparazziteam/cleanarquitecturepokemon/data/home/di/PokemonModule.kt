package com.paparazziteam.cleanarquitecturepokemon.data.home.di

import com.apollographql.apollo3.ApolloClient
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonGraphQlDataSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonRemoteDataSource
import com.paparazziteam.cleanarquitecturepokemon.data.home.remote.PokemonService
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


    //Repository
    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: PokemonRemoteDataSource,
        graphQlDataSource: PokemonGraphQlDataSource
    ) = PokemonRepository(remoteDataSource,graphQlDataSource)


}