package com.example.test10.presenter.screen.home


import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.test10.databinding.FragmentHomeBinding
import com.example.test10.presenter.base.BaseFragment
import com.example.test10.presenter.extensions.showSnackBar
import com.example.test10.presenter.screen.home.bottom_sheet.BottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {



    override fun bind() {

    }

    override fun bindViewActionListeners() {
        binding.addBtn.setOnClickListener {
            showBottomSheet()
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getContent.launch(intent)
    }

    private var getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                val bitmask = processSelectedImage(uri)
                binding.image.setImageBitmap(bitmask)
            }
        }else{
            binding.root.showSnackBar("Empty")
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            getContent1.launch(intent)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        }
    }

    private val getContent1 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageBitmap = compressBitmap((result.data?.extras?.get("data") as? Bitmap)!!)
            binding.image.setImageBitmap(imageBitmap)
        }
    }
    private fun showBottomSheet() {
        val bottomSheetFragment = BottomSheetFragment().apply {
            setListener(object : BottomSheetFragment.BottomSheetListener {
                override fun onOptionSelected(option: String) {
                    when (option) {
                        "TAKE_PICTURE" -> openCamera()
                        "CHOOSE_GALLERY" -> chooseFromGallery()
                    }
                }
            })
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun chooseFromGallery() {
        openGallery()
    }

    private fun processSelectedImage(uri: Uri): Bitmap {

        val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)

        return compressBitmap(bitmap)
    }

}



