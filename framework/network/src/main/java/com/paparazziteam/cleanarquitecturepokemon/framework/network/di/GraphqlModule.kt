package com.paparazziteam.cleanarquitecturepokemon.framework.network.di

import com.apollographql.apollo3.ApolloClient
import com.paparazziteam.cleanarquitecturepokemon.framework.network.Constans
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GraphqlModule {

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(Constans.BASE_URL_GRAPHQL)
            .build()
    }
}