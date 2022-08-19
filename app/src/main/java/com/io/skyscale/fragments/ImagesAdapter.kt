package com.io.skyscale.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.io.skyscale.R
import com.io.skyscale.databinding.ImageItemContainerBinding

class ImagesAdapter(
    context: Context,
    private val imageItems: List<Image>,
    private val clickHandler: (Image) -> Unit
) : RecyclerView.Adapter<ImagesAdapter.ListViewHolder>() {

    inner class ListViewHolder(binding: ImageItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageItemView: ImageView = binding.itemImage

        fun bindImageItem(imageItem: Image) = imageItemView.setImageURI(imageItem.image)
    }

    companion object : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }

    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView =
            ImageItemContainerBinding.inflate(inflater, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bindImageItem(imageItems[position])
        holder.itemView.setOnClickListener {
            clickHandler(imageItems[position])
        }
    }

    override fun getItemCount(): Int = imageItems.size
}
