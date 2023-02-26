package com.paparazziteam.cleanarquitecturepokemon.feature.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import com.paparazziteam.cleanarquitecturepokemon.domain.toParcelable
import com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters.PokemonTeamAdapter
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ActivityPokemonTeamBinding
import com.paparazziteam.cleanarquitecturepokemon.feature.home.dialogs.BottomSheetDialogFragment
import com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels.HomeViewModel
import com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels.PokemonTeamViewModel
import com.paparazziteam.cleanarquitecturepokemon.shared.base.BaseActivity
import com.paparazziteam.cleanarquitecturepokemon.shared.components.VerticalSpacingItemDecoration
import com.paparazziteam.cleanarquitecturepokemon.shared.databinding.CustomDialogCopyTeamBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.preventDoubleLongClick
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.setMaxLength
import dagger.hilt.android.AndroidEntryPoint
import pe.com.tarjetaw.android.client.shared.network.Event


@AndroidEntryPoint
class PokemonTeamActivity : BaseActivity<ActivityPokemonTeamBinding>(ActivityPokemonTeamBinding::inflate) {

    private val viewModel by viewModels<PokemonTeamViewModel>()
    private val viewModelHome by viewModels<HomeViewModel>()

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
        viewModel.eventsByToken.observe(this, Observer(::handleEventsByToken))
        viewModelHome.eventsCreateTeam.observe(this, Observer(::handleEventsCreateTeam))
    }

    private fun handleEventsCreateTeam(event: Event<HomeViewModel.CreateTeamState>?) {
        event?.getContentIfNotHandled()?.let {
            when(it){
                is HomeViewModel.CreateTeamState.Error -> it.run{
                    Toast.makeText(this@PokemonTeamActivity, message, Toast.LENGTH_SHORT).show()
                }
                HomeViewModel.CreateTeamState.HideLoading -> {}
                HomeViewModel.CreateTeamState.ShowLoading -> {}
                is HomeViewModel.CreateTeamState.Success -> it.run {
                    Toast.makeText(this@PokemonTeamActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handleEventsByToken(event: Event<PokemonTeamViewModel.PokemonTeamByTokenEvent>?) {
        event?.getContentIfNotHandled()?.let {
            when (it) {
                is PokemonTeamViewModel.PokemonTeamByTokenEvent.ShowLoading -> {}
                is PokemonTeamViewModel.PokemonTeamByTokenEvent.Error -> it.run {
                    Toast.makeText(this@PokemonTeamActivity, error, Toast.LENGTH_SHORT).show()
                }
                PokemonTeamViewModel.PokemonTeamByTokenEvent.HideLoading -> {}
                is PokemonTeamViewModel.PokemonTeamByTokenEvent.Success -> it.run {
                    Toast.makeText(this@PokemonTeamActivity, "Pokemon team founded - Now we are making a copy ", Toast.LENGTH_SHORT).show()
                    viewModelHome.copyTeam(pokemonTeam, userId)
                }
            }
        }
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


    fun createTeamDialog(): Dialog {
        var customBinding = CustomDialogCopyTeamBinding.inflate(LayoutInflater.from(this))

        return Dialog(this).apply{
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(customBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        }.also { dialog->
            customBinding.etToken.setMaxLength(8)
            customBinding.btnOk.setOnClickListener {
                it.preventDoubleLongClick()
                val token = customBinding.etToken.text.toString()
                if(token.isEmpty()){
                    Toast.makeText(this, "Token must not be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(token.length != 8){
                    Toast.makeText(this, "Token must be 8 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.getPokemonTeamByToken(token)
                dialog.dismiss()
            }
            dialog.show()
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

        binding.fabPokemonTeamCopy.setOnClickListener {
            //dialog
            createTeamDialog()
        }
    }

}