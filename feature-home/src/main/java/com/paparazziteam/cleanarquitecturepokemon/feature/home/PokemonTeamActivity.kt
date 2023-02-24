package com.paparazziteam.cleanarquitecturepokemon.feature.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityPokemonTeamBinding
import com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels.PokemonTeamViewModel
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonTeamActivity : BaseActivity<ActivityPokemonTeamBinding>(ActivityPokemonTeamBinding::inflate) {

    private val viewModel by viewModels<PokemonTeamViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        listeners()
    }

    private fun listeners() {
        binding.ivPokemonTeamBack.setOnClickListener {
            finish()
        }
    }
}