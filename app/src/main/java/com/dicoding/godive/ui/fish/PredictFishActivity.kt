package com.dicoding.godive.ui.fish

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.godive.R
import com.dicoding.godive.data.fish.ImageClassifier
import java.io.IOException

class PredictFishActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_TAKE_PHOTO = 101
    private val CAMERA_PERMISSION_REQUEST_CODE = 102
    private lateinit var imageView: ImageView
    private var selectedImageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_fish)

        imageView = findViewById(R.id.imageView)
        val pickImageButton: Button = findViewById(R.id.btnGallery)
        val takePhotoButton: Button = findViewById(R.id.btnCamera)
        val predictButton: Button = findViewById(R.id.btnPredict)

        // Button to pick image from gallery
        pickImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Button to take a photo using the camera
        takePhotoButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            } else {
                // Request permission to use camera
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE)
            }
        }

        // Button to predict the image
        predictButton.setOnClickListener {
            if (selectedImageBitmap != null) {
                val classifier = ImageClassifier(this)
                val prediction = classifier.classifyImage(selectedImageBitmap!!)

                val intent = Intent(this, ResultPredictActivity::class.java)
                intent.putExtra("prediction", prediction)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle back button click
        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()  // Close the activity
        }
    }

    // Handle the result from the image picker or camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                val imageUri: Uri = data?.data ?: return
                try {
                    selectedImageBitmap = android.provider.MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    imageView.setImageBitmap(selectedImageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                selectedImageBitmap = data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(selectedImageBitmap)
            }
        }
    }

    // Handle the result of permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open camera
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Izin kamera diperlukan untuk mengambil foto", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
