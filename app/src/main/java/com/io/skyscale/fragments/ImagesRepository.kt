package com.io.skyscale.fragments

import android.net.Uri
import com.io.skyscale.dir

object ImagesRepository {
    val imagesItems: MutableList<Image> = mutableListOf()

    init {
        addImagesToList()
    }

    private fun addImagesToList() {

        val files = dir.listFiles()
        files?.filter { it.canRead() && it.isFile }?.map {
            val name = it.name
            val uri = Uri.fromFile(it)
            imagesItems.add(0, Image(name, uri))
        } ?: listOf()
    }
}