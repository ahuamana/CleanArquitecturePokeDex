package com.paparazziteam.cleanarquitecturepokemon

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokeApp :Application() {
    override fun onCreate() {
        super.onCreate()
        //For Dark Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}