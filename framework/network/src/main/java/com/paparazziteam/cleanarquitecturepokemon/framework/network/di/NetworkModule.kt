package com.paparazziteam.cleanarquitecturepokemon.framework.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Singleton
    @Provides
    fun provideCertificatePinner():CertificatePinner =CertificatePinner.Builder()
        //.add("tarjetaw.com", "sha256/........")
        .build()

    @Singleton
    @Provides
    fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, certificatePinner: CertificatePinner): OkHttpClient = OkHttpClient.Builder()
        //.certificatePinner(certificatePinner) // this is for pinning when you have a certificate public key hashes
        .addInterceptor(httpLoggingInterceptor)
        .followRedirects(false)
        .followSslRedirects(false)
        .build()
}

