package com.example.moviematcher.navigationbar.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.moviematcher.MainViewModel

import com.example.moviematcher.databinding.SettingsBinding
import com.example.moviematcher.login.LoginScreen
import com.example.moviematcher.navigationbar.friends.FriendsAdapter
import com.github.ajalt.timberkt.v


class settings : Fragment() {

    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_PERMISSION_CODE = 2
    private lateinit var binding: SettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel : MainViewModel by activityViewModels()
        // Set onClickListener for the imageView
        binding.imageView.setOnClickListener {
            // Check for READ_EXTERNAL_STORAGE permission
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image gallery
                openImageGallery()

            } else {
                // Permission not granted, request permission
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_CODE)
            }
        }



        binding.SignOutButton.setOnClickListener(){
            viewModel.signOut()
            val intent = Intent(requireContext(), LoginScreen::class.java)
            startActivity(intent)
        }

        binding.ChangeButton.setOnClickListener(){
            viewModel.changePassword(binding.OldPassword.text.toString(),binding.newPassword.text.toString(),requireContext());

        }




    }

    private fun openImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data?.data
            if (selectedImage != null) {
                Glide.with(this)
                    .load(selectedImage)
                    .into(binding.imageView)
                binding.textView.visibility =View.GONE
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, open image gallery
            openImageGallery()
        }
    }


}
