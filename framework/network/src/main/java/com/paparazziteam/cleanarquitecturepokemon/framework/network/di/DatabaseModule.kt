package com.paparazziteam.cleanarquitecturepokemon.framework.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paparazziteam.cleanarquitecturepokemon.framework.network.Constans
import com.paparazziteam.cleanarquitecturepokemon.framework.network.Constans.RETROFIT_BUILDER_POKEAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton@Provides
    fun provideGson():Gson  = GsonBuilder().create()


    @Provides
    //@Named(RETROFIT_BUILDER_POKEAPI)
    fun provideRetrofitPromotions(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Constans.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        //.addCallAdapterFactory(RootCallAdapterFactory(rootBear))
        .build()
}