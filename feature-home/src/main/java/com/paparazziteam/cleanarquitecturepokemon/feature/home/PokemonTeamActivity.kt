package com.paparazziteam.cleanarquitecturepokemon.feature.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import com.paparazziteam.cleanarquitecturepokemon.domain.toParcelable
import com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters.PokemonTeamAdapter
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityPokemonTeamBinding
import com.paparazziteam.cleanarquitecturepokemon.feature.home.dialogs.BottomSheetDialogFragment
import com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels.PokemonTeamViewModel
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import com.paparazziteam.cleanarquitecturepokemon.shared.components.VerticalSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import pe.com.tarjetaw.android.client.shared.network.Event


@AndroidEntryPoint
class PokemonTeamActivity : BaseActivity<ActivityPokemonTeamBinding>(ActivityPokemonTeamBinding::inflate) {

    private val viewModel by viewModels<PokemonTeamViewModel>()

    private var userId = ""

    private val mAdapter by lazy {
        PokemonTeamAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        extras()
        setupRecyclerView()
        listeners()
        observers()
    }

    private fun observers() {
        viewModel.events.observe(this, Observer(::handleEvents))
    }

    private fun createBottomSheetDialog(team: PokemonTeam) {
        val bunble = Bundle()
        bunble.putParcelable("team", team.toParcelable())

        val bottomSheetDialog = BottomSheetDialogFragment()
        bottomSheetDialog.arguments = bunble
        bottomSheetDialog.show(supportFragmentManager, "BottomSheetDialogFragment")
    }

    private fun handleEvents(event: Event<PokemonTeamViewModel.PokemonTeamEvent>?) {
        event?.getContentIfNotHandled()?.let {
            when (it) {
                is PokemonTeamViewModel.PokemonTeamEvent.ShowLoading -> {

                }
                is PokemonTeamViewModel.PokemonTeamEvent.Error -> it.run {
                    println("Error pokemon teams: $error")
                }
                PokemonTeamViewModel.PokemonTeamEvent.HideLoading -> {

                }
                is PokemonTeamViewModel.PokemonTeamEvent.Success -> it.run {
                    print("Success pokemon teams: $pokemonTeam")
                    mAdapter.submitList(pokemonTeam)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvPokemonTeam.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@PokemonTeamActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalSpacingItemDecoration(20,true))
        }

        mAdapter.setOnClickListener {
            createBottomSheetDialog(it)
        }
    }



    private fun extras() {
        userId = intent.getStringExtra("email") ?: ""
        viewModel.getTeamsByUser(userId)
    }

    private fun listeners() {
        binding.ivPokemonTeamBack.setOnClickListener {
            finish()
        }
    }
}