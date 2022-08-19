package com.io.skyscale.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.io.skyscale.R

class ClickedImageFragment : Fragment(R.layout.fragment_home) {

    private val args: ClickedImageFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_clicked_image, container, false)

        val imagesViewPager: ViewPager2 = layout.findViewById(R.id.vp_clicked_image_view)
        imagesViewPager.adapter = ViewPagerAdapter(requireContext(), ImagesRepository.imagesItems)
        imagesViewPager.setCurrentItem(args.imageIndex, false)
        imagesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return layout
    }

}