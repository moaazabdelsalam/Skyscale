package com.io.skyscale.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.io.skyscale.R
import com.io.skyscale.databinding.FragmentUpscaleBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val GALLERY_REQUEST_CODE = 20
private const val CAMERA_REQUEST_CODE = 21

class UpscaleFragment : Fragment(R.layout.fragment_upscale) {

    private lateinit var binding: FragmentUpscaleBinding

    lateinit var previewImage: ImageView
    lateinit var currentPhotoPath: String
    lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpscaleBinding.inflate(inflater, container, false)
        val view = binding.root

        val fabCamera = binding.fabCamera
        val fabGallery = binding.fabGallery
        previewImage = binding.ivPreviewImage

        fabCamera.setOnClickListener {
            if (hasPermissions(
                    android.Manifest.permission.CAMERA,
                    CAMERA_REQUEST_CODE
                )
            )
                captureImage()
            else
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
        }

        fabGallery.setOnClickListener {
            if (hasPermissions(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    GALLERY_REQUEST_CODE
                )
            )
                pickImage()
            else
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_REQUEST_CODE
                )
        }
        return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    pickImage()
                else
                    Toast.makeText(
                        requireContext(),
                        "Need storage permission to open gallery!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    captureImage()
                else
                    Toast.makeText(
                        requireContext(),
                        "Need camera permission to open camera!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun hasPermissions(permission: String, requestCode: Int) =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun pickImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    private fun captureImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            Log.e("TAG", "openCamera: error occurred")
        }

        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.io.skyscale.android.fileprovider", photoFile
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = (activity as AppCompatActivity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK)
                    imageUri = data?.data!!
                else {
                    Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val file = File(currentPhotoPath)
                    imageUri = Uri.fromFile(file)
                } else {
                    Toast.makeText(requireContext(), "No image captured", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
        navigateToChooseService()
    }

    private fun navigateToChooseService() {
        val action =
            UpscaleFragmentDirections.actionUpscaleFragmentToChooseServiceFragment(imageUri.toString())
        findNavController().navigate(action)
    }
}