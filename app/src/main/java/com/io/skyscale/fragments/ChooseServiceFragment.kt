package com.io.skyscale.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.io.skyscale.R
import com.io.skyscale.SERVER_IP
import com.io.skyscale.SERVER_TOKEN
import com.io.skyscale.api.OtherFeaturesPostRequest
import com.io.skyscale.api.UpscalePostRequest
import com.io.skyscale.api.ResultImage
import com.io.skyscale.api.createSkyscaleService
import com.io.skyscale.databinding.FragmentChooseServiceBinding
import com.io.skyscale.dir
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChooseServiceFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentChooseServiceBinding

    private val args: ChooseServiceFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentChooseServiceBinding.inflate(inflater, container, false)

        binding.ivChooseService.setImageURI(Uri.parse(args.image))

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_list_item,
            resources.getStringArray(R.array.services_list)
        )
        binding.autoCompleteTV.setAdapter(adapter)

        binding.btnConnectToServer.setOnClickListener {
            createPostRequest(convertImageToBase64())
        }

        binding.btnCancelChooseService.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSaveResultImage.setOnClickListener {
            saveResultPhotoToDeviceStorage()
        }

        return binding.root
    }

    private fun convertImageToBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bitmap = binding.ivChooseService.drawable.toBitmap()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun createPostRequest(imageBytes: String): Any {
        return when (binding.autoCompleteTV.text.toString()) {
            "Upscale 4X" -> sendImageToServer(
                createSkyscaleService().upscalePostRequest(
                    UpscalePostRequest(imageBytes, "4", "Upscale", SERVER_TOKEN)
                )
            )
            "Colorize" -> sendImageToServer(
                createSkyscaleService().otherFeaturesPostRequest(
                    OtherFeaturesPostRequest(imageBytes, "Colorize", SERVER_TOKEN)
                )
            )
            "Remove Background" -> sendImageToServer(
                createSkyscaleService().otherFeaturesPostRequest(
                    OtherFeaturesPostRequest(imageBytes, "RemoveBG", SERVER_TOKEN)
                )
            )
            else -> sendImageToServer(
                createSkyscaleService().upscalePostRequest(
                    UpscalePostRequest(
                        imageBytes,
                        "2",
                        "Upscale",
                        SERVER_TOKEN
                    )
                )
            )
        }
    }

    private fun sendImageToServer(call: Call<ResultImage>) {

        Toast.makeText(
            requireContext(),
            "Uploading photo for ${binding.autoCompleteTV.text}",
            Toast.LENGTH_SHORT
        ).show()
        binding.btnConnectToServer.apply {
            visibility = View.GONE
            isEnabled = false
            isClickable = false
        }


        call.enqueue(object : Callback<ResultImage> {

            override fun onResponse(call: Call<ResultImage>, response: Response<ResultImage>) {
                val serverResponse = response.body()
                if (serverResponse != null) {

                    Log.d(
                        ChooseServiceFragment::class.java.simpleName,
                        "response: ${serverResponse.imageUrl}"
                    )
                    binding.ivChooseService.load("$SERVER_IP${serverResponse.imageUrl}")
                    binding.btnSaveResultImage.apply {
                        visibility = View.VISIBLE
                        isEnabled = true
                        isClickable = true
                    }
                }
            }

            override fun onFailure(call: Call<ResultImage>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to connect to server", Toast.LENGTH_SHORT)
                    .show()
                Log.e(ChooseServiceFragment::class.java.simpleName, "error getting to server", t)
            }
        })
    }

    private fun saveResultPhotoToDeviceStorage() {

        val bitmap = binding.ivChooseService.drawable.toBitmap()

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

        try {
            FileOutputStream(
                File.createTempFile(imageFileName, ".jpg", dir)
            ).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            }
        } catch (e: IOException) {
            Log.e("Save", "file wasn't saved: $imageFileName")
        }
        val image = File(dir.toString() + imageFileName)

        Toast.makeText(requireContext(), "Image saved $image", Toast.LENGTH_SHORT).show()

        galleryAddPic(requireContext())
    }

    private fun galleryAddPic(context: Context) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(dir.toString())
            mediaScanIntent.data = Uri.fromFile(f)
            context.sendBroadcast(mediaScanIntent)
        }
    }

}