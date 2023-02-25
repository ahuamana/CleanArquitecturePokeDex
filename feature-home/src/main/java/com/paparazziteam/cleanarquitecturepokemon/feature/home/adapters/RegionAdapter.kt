package com.paparazziteam.cleanarquitecturepokemon.feature.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paparazziteam.cleanarquitecturepokemon.domain.Region
import com.paparazziteam.cleanarquitecturepokemon.feature.home.R
import com.paparazziteam.cleanarquitecturepokemon.feature.home.databinding.ItemRegionBinding
import com.paparazziteam.cleanarquitecturepokemon.shared.utils.setBackgroundResourceWithTint

class RegionAdapter : ListAdapter<Region, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Region>()
        {
            //verifica si el objeto (o la fila de la base de datos, en tu caso) es el mismo que verifica el ID
            override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
                return oldItem.name == newItem.name
            }
            //verifica si todas las propiedades, no solo el ID, son iguales.
            override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var onItemClick: ((Region, position:Int) -> Unit)? = null
    fun onItemClick(listener:(Region, position:Int)-> Unit){
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            ItemRegionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }


    inner class CategoryViewHolder( private val binding: ItemRegionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Region, position: Int) {
            binding.title.text = item.name

            itemView.apply {
                val backgroundResource = if (item.isSelected) R.drawable.rounder_button_no_color else R.drawable.corner_button_outline
                val textColor = if (item.isSelected) com.paparazziteam.cleanarquitecturepokemon.shared.R.color.colorWhite else com.paparazziteam.cleanarquitecturepokemon.shared.R.color.colorSecondary

                binding.title.apply { setBackgroundResourceWithTint(this,backgroundResource, com.paparazziteam.cleanarquitecturepokemon.shared.R.color.colorSecondary) }
                binding.title.setTextColor(context.getColor(textColor))

                itemView.setOnClickListener {
                    onItemClick?.invoke(item, position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)?: return
        (holder as CategoryViewHolder).bind(item, position)
    }
}