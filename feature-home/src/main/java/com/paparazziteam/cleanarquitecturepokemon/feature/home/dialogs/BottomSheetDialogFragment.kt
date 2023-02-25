package com.paparazziteam.cleanarquitecturepokemon.feature.home.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponseParcelable
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeamParcelable
import com.paparazziteam.cleanarquitecturepokemon.domain.toDomain
import com.paparazziteam.cleanarquitecturepokemon.feature.home.viewmodels.BottomSheetDialogFragmentViewModel
import com.paparazziteam.cleanarquitecturepokemon.shared.base.autoCleared
import com.paparazziteam.cleanarquitecturepokemon.shared.databinding.BottomSheetOptionsBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.copyToClipboard
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.getParcelableObject
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var binding: BottomSheetOptionsBinding by autoCleared()

    private val viewModel by viewModels<BottomSheetDialogFragmentViewModel>()

    private var pokemonTeamParcelable: PokemonTeamParcelable? = null
    private var pokemonSelected: PokemonResponseParcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonTeamParcelable = it.getParcelableObject("team")
        }

       pokemonTeamParcelable?.pokemon?.let {
            //get pokemon that is not selected
           pokemonSelected = it.filter { it.isSelected }.first()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetOptionsBinding.inflate(layoutInflater,container, false)

        setupExtras()
        listeners()

        return binding.root
    }

    private fun listeners() {
        binding.cardViewShare.setOnClickListener {
            copyToClipboard(requireContext(),pokemonTeamParcelable?.metaData?.token?:"")
            Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }

        binding.cardViewDelete.setOnClickListener {
            //remove pokemon from team
            pokemonSelected?.toDomain()
                ?.let { pokemon -> pokemonTeamParcelable?.id?.let { pokemonTeamId ->
                    viewModel.removePokemonFromTeam(pokemonTeamId, pokemon) }
                }
            dismiss()
        }
    }

    private fun setupExtras() {
        pokemonSelected?.let {
            binding.pokemonName.text = it.name
            binding.pokemonImage.loadImage(it.url)
            binding.pokemonType.text = it.tipo.first()
            binding.pokemonNumber.text = it.order.toString()
            binding.pokemonDescriptionShort.text = it.description
            binding.tvToken.text = pokemonTeamParcelable?.metaData?.token?:""
        }
    }
}