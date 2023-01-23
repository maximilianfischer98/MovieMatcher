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
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.v
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class settings : Fragment() {

    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_PERMISSION_CODE = 2
    private lateinit var binding: SettingsBinding

    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mDatabase: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsBinding.inflate(inflater, container, false)
        mDatabase = Firebase.database.reference

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

        viewModel.getCurrentUsername { username ->
            val rootRef = mDatabase.child("users").child(username)
            rootRef.child("profileImage").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.e(null,{"Failed to get image url from Database"})
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val imageUrl = snapshot.value.toString()
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .into(binding.imageView)
                        binding.textView.visibility = View.GONE
                    }
                }
            })
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
                //Create a reference to the storage location
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("profilpic/${UUID.randomUUID()}")
                imageRef.putFile(selectedImage)
                    .addOnSuccessListener { taskSnapshot ->
                        // Get the download URL
                        val downloadUrl = taskSnapshot.storage.downloadUrl
                        downloadUrl.addOnSuccessListener { url ->
                            // Store the download URL in the Firebase Realtime Database
                            val viewModel : MainViewModel by activityViewModels()
                            viewModel.getCurrentUsername {username ->
                                val rootRef = mDatabase.child("users")
                                val currentUserRef = rootRef.child(username)
                                currentUserRef.child("profileImage").setValue(url.toString())
                            }
                        }
                    }
                    .addOnFailureListener {
                        // Handle failure
                    }
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
