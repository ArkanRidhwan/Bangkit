package com.inc.vr.corps.storymaps.view.ui.islogin.add


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.inc.vr.corps.storymaps.BuildConfig
import com.inc.vr.corps.storymaps.R
import com.inc.vr.corps.storymaps.databinding.FragmentAddBinding
import com.inc.vr.corps.storymaps.databinding.FragmentLoginBinding
import com.inc.vr.corps.storymaps.databinding.FragmentStoryBinding
import com.inc.vr.corps.storymaps.helper.Helper
import com.inc.vr.corps.storymaps.view.ui.islogin.MainActivity
import com.inc.vr.corps.storymaps.view.ui.nologin.welcome.WelcomeFragmentDirections
import com.inc.vr.corps.storymaps.viewmodel.StoryViewModel
import com.inc.vr.corps.storymaps.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.io.File


@AndroidEntryPoint
class AddFragment :  Fragment(R.layout.fragment_add) {
    private lateinit var binding: FragmentAddBinding
    private val userViewModel by viewModels<UserViewModel>()
    private val storyViewModel by viewModels<StoryViewModel>()
    private lateinit var photoPath: String
    private var imgFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireContext().getTheme().applyStyle(androidx.constraintlayout.widget.R.style.Theme_AppCompat_DayNight_DarkActionBar, true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
            //mengganti theme
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Timber BuildConfig
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        setupPermission()
        setupAction()
        getToken()

        storyViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    private fun setupPermission() {
        if (!allPermissionsGrant()) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSION_REQUIRED, REQUEST_CODE)
        }
    }


    private fun getToken() {
        userViewModel.getUserPreferences().observe(requireActivity()) {
            if (it.token.trim() != "") {
                EXTRA_TOKEN = it.token
            }
        }
    }

    private fun setupAction() {
        if (imgFile == null) {
            Glide.with(this).load(imgFile).placeholder(R.drawable.ic_baseline_image_24)
                .fallback(R.drawable.ic_baseline_image_24).into(binding.imgUpload)
        }

        binding.apply {
            btnCamera.setOnClickListener {
                accessCamera()
            }
            btnGallery.setOnClickListener {
                accessGallery()
            }
            btnUpload.setOnClickListener {
                uploadStory()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (!allPermissionsGrant()) {
                Toast.makeText(requireContext(), "Silakan Berikan Permission.", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGrant() = PERMISSION_REQUIRED.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun accessCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)
        Helper.createFileTemp(requireActivity()).also {
            val photoUri: Uri =
                FileProvider.getUriForFile(requireContext(), "com.inc.vr.corps", it)
            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launchCamera.launch(intent)
        }
    }

    private val launchCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                Glide.with(this).load(photoPath).placeholder(R.drawable.ic_baseline_image_24)
                    .fallback(R.drawable.ic_baseline_image_24).into(binding.imgUpload)
                imgFile = File(photoPath)
            } else {
                Toast.makeText(requireContext(), "Gagal Mengambil Foto", Toast.LENGTH_SHORT).show()
            }
        }

    private fun accessGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }

        val imgChooser = Intent.createChooser(intent, "Choose Picture")
        launchGallery.launch(imgChooser)
    }

    private val launchGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val selectedImage: Uri = it.data?.data as Uri
                val file = Helper.uriToFile(selectedImage, requireContext())
                Timber.d("File: $file")
                imgFile = file
                Glide.with(this).load(selectedImage).into(binding.imgUpload)
            }
        }

    private fun uploadStory() {
        val description = binding.editDescription.text.toString()

        when {
            imgFile == null -> {
                Toast.makeText(
                    context,
                    "Silakan masukkan berkas image/gambar.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            description.trim().isEmpty() -> {
                Toast.makeText(context, "Silakan masukkan deskripsi.", Toast.LENGTH_SHORT).show()
                binding.editDescription.error = "Field description tidak boleh kosong"
            }
            else -> {
                val file = Helper.reduceImageSize(imgFile as File)

                storyViewModel.uploadStory(EXTRA_TOKEN, description, file)

                storyViewModel.responseUpload.observe(requireActivity()) { response ->
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    if (!response.error) {
                        //move nav to story
                        val action = AddFragmentDirections.actionAddFragmentToStoryFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }

        binding.apply {
            binding.btnCamera.isEnabled = !isLoading
            binding.btnUpload.isEnabled = !isLoading
            binding.btnGallery.isEnabled = !isLoading
            binding.editDescription.isEnabled = !isLoading
        }
    }

    companion object {
        private var EXTRA_TOKEN = "token"

        private val PERMISSION_REQUIRED = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE = 10
    }
}