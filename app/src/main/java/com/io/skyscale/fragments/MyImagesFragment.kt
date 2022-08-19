package com.io.skyscale.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.io.skyscale.R

class MyImagesFragment : Fragment(R.layout.fragment_my_images) {

    private val imagesItems: MutableList<Image> = ImagesRepository.imagesItems
    private lateinit var imagesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_my_images, container, false)

        //initialize the RecyclerView using staggered grid
        imagesRecyclerView = layout.findViewById(R.id.rv_images)
        imagesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        loadPhotosToRecyclerView()

        return layout
    }

    private fun loadPhotosToRecyclerView() {
        imagesRecyclerView.adapter =
            ImagesAdapter(requireContext(), imagesItems) { image ->
                showClickedImage(imagesItems.indexOf(image))
            }
    }

    private fun showClickedImage(imageIndex: Int) {
        val action =
            MyImagesFragmentDirections.actionMyImagesFragmentToClickedImageFragment(imageIndex)
        findNavController().navigate(action)
    }

}