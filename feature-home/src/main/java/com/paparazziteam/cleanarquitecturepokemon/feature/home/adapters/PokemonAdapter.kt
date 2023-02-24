package com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.cleanarquitecturepokemon.domain.PokemonResponse
import com.paparazziteam.cleanarquitecturepokemon.feature.home.R
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ItemPokemonBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.loadImage
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.removeCardBackgroundColor
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.setCardBackgroundColorWithAlpha
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.toShortDescription

class PokemonAdapter : ListAdapter<PokemonResponse, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PokemonResponse>() {
            //verifica si el objeto (o la fila de la base de datos, en tu caso) es el mismo que verifica el ID
            override fun areItemsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse): Boolean {
                return oldItem.name == newItem.name
            }

            //verifica si todas las propiedades, no solo el ID, son iguales.
            override fun areContentsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClick: ((PokemonResponse, position: Int) -> Unit)? = null
    fun onItemClick(listener: (PokemonResponse, position: Int) -> Unit) {
        onItemClick = listener
    }

    fun selectItem(position: Int) {
        val item = getItem(position)
        item.isSelected = !item.isSelected
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PokemonViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    inner class PokemonViewHolder(private val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PokemonResponse, position: Int) {
            binding.pokemonName.text = item.name
            binding.pokemonImage.loadImage(item.url)
            binding.pokemonNumber.text = item.order.toString()
            binding.pokemonType.text = item.tipo.first()
            binding.pokemonDescriptionShort.text = item.description.toShortDescription()

            itemView.apply {
                itemView.setOnClickListener {
                    onItemClick?.invoke(item, position)
                }
            }

            if(item.isSelected) {
                binding.root.setCardBackgroundColorWithAlpha(com.paparazziteam.cleanarquitecturepokemon.shared.R.color.colorSecondary, 0.5f)
            } else {
                binding.root.removeCardBackgroundColor()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as PokemonViewHolder).bind(item, position)
    }
}