package com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonTeam
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ItemPokemonTeamBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.components.HorizontalSpacingItemDecoration

class PokemonTeamAdapter: ListAdapter<PokemonTeam, RecyclerView.ViewHolder>(PokemonTeamDiffUtil) {

    companion object {
        private val PokemonTeamDiffUtil = object : DiffUtil.ItemCallback<PokemonTeam>() {
            override fun areItemsTheSame(oldItem: PokemonTeam, newItem: PokemonTeam): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PokemonTeam, newItem: PokemonTeam): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onClickListener: ((PokemonTeam) -> Unit)? = null
    fun setOnClickListener(listener: (PokemonTeam) -> Unit) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PokemonTeamViewHolder(
            ItemPokemonTeamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemonTeam = getItem(position) ?: return
        (holder as PokemonTeamViewHolder).bind(pokemonTeam)
    }

    inner class PokemonTeamViewHolder(private val binding: ItemPokemonTeamBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pokemonTeam: PokemonTeam) {
           binding.apply {
                tvPokemonTeamNameDescription.text = pokemonTeam.name
               val mAdapter = PokemonAdapter()
                binding.rvPokemonTeamPokemon.apply {
                    adapter = mAdapter
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    addItemDecoration(HorizontalSpacingItemDecoration(10))
                }
                mAdapter.submitList(pokemonTeam.pokemon)
                mAdapter.notifyDataSetChanged()

                mAdapter.onItemClick { pokemonResponse, position ->
                    var copy = pokemonTeam.copy(pokemon = pokemonTeam.pokemon?.map { pokemon ->
                        pokemon.isSelected = pokemon.name == pokemonResponse.name
                        pokemon
                    })
                    onClickListener?.invoke(copy)
                }
           }
        }
    }
}