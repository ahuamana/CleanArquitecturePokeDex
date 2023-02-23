package com.paparazziteam.cleanarquitecturepokemon.feature.home

import android.os.Bundle
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityHomeBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}