package com.dicoding.godive.ui.profile

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dicoding.godive.R
import com.dicoding.godive.data.login.response.ProfileUser
import com.dicoding.godive.data.login.retrofite.RetrofitInstancee
import com.dicoding.godive.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var profileImageView: ImageView

    // Request code untuk memilih gambar dari galeri
    private val PICK_IMAGE_REQUEST = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menemukan TextView untuk nama, email dan ImageView untuk profil
        nameTextView = view.findViewById(R.id.nameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        profileImageView = view.findViewById(R.id.profileImageView)

        // Mendapatkan tombol logout
        val logoutButton: Button = view.findViewById(R.id.logoutBtn)

        // Set listener untuk tombol logout
        logoutButton.setOnClickListener {
            logoutUser()
        }

        // Set listener untuk memilih gambar dari galeri
        profileImageView.setOnClickListener {
            openGallery()
        }

        // Ambil data profil pengguna dari API
        getProfileData()
    }

    private fun getProfileData() {
        CoroutineScope(Dispatchers.IO).launch {
            val sharedPref = requireActivity().getSharedPreferences("USER_PREF", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)

            if (token != null) {
                // Mengambil data profil menggunakan token yang valid
                val response: Response<List<ProfileUser>> = RetrofitInstancee.api.getUsers(token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val users = response.body()
                        if (users != null && users.isNotEmpty()) {
                            // Menampilkan data profil pertama yang ditemukan
                            val profileUser = users.first()  // Ambil user pertama dalam array
                            nameTextView.text = profileUser.name
                            emailTextView.text = profileUser.email
                        } else {
                            Toast.makeText(activity, "Error: No profile data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Failed to load profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Fungsi untuk membuka galeri dan memilih gambar
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Menangani hasil pemilihan gambar dari galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data!!
            try {
                // Menampilkan gambar yang dipilih di ImageView
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                profileImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logoutUser() {
        // Menghapus token dari SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("USER_PREF", MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("TOKEN")  // Menghapus token
            apply()
        }

        // Menampilkan pesan logout berhasil
        Toast.makeText(activity, "Logout successful", Toast.LENGTH_SHORT).show()

        // Mengalihkan pengguna kembali ke halaman Login
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()  // Menutup halaman ProfileFragment dan mengakhiri sesi aplikasi
    }
}
