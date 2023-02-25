package com.paparazziteam.cleanarquitecturepokemon.feature.home.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponseParcelable
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeamParcelable
import com.paparazziteam.cleanarquitecturepokemon.shared.base.autoCleared
import com.paparazziteam.cleanarquitecturepokemon.shared.databinding.BottomSheetOptionsBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.getParcelableObject
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.loadImage

class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var binding: BottomSheetOptionsBinding by autoCleared()

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


        return binding.root
    }

    private fun setupExtras() {
        pokemonSelected?.let {
            binding.pokemonName.text = it.name
            binding.pokemonImage.loadImage(it.url)
            binding.pokemonType.text = it.tipo.first()
            binding.pokemonNumber.text = it.order.toString()
            binding.pokemonDescriptionShort.text = it.description
        }
    }
}