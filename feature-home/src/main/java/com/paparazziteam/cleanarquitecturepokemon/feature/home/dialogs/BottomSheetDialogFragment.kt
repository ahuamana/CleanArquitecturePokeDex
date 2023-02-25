package com.paparazziteam.cleanarquitecturepokemon.feature.home.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeamParcelable
import com.paparazziteam.cleanarquitecturepokemon.shared.base.autoCleared
import com.paparazziteam.cleanarquitecturepokemon.shared.databinding.BottomSheetOptionsBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.getParcelableObject

class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var binding: BottomSheetOptionsBinding by autoCleared()

    private var pokemonTeamParcelable: PokemonTeamParcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonTeamParcelable = it.getParcelableObject("team")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetOptionsBinding.inflate(layoutInflater,container, false)




        return binding.root
    }
}