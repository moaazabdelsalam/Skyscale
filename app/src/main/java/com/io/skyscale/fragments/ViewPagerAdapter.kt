package com.io.skyscale.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.io.skyscale.R

class ViewPagerAdapter(
    context: Context,
    private val images: List<Image>
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewPagerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val displayClickedImageView: ImageView =
            itemView.findViewById(R.id.iv_display_clicked_image)

        fun bindImageItem(imageItem: Image) =
            displayClickedImageView.setImageURI(imageItem.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val itemView = inflater.inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curImage = images[position]
        holder.bindImageItem(curImage)
    }

    override fun getItemCount(): Int = images.size

}