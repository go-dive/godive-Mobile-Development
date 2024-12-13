package com.dicoding.godive.ui.equipment

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.godive.R
import com.dicoding.godive.data.equipment.EquipmentAdapter
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager

class PredictEquipmentActivity : AppCompatActivity() {
    private lateinit var interpreter: Interpreter
    private lateinit var imageView: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonOpenCamera: Button  // Button untuk open camera
    private lateinit var buttonPredict: Button
    private lateinit var buttonNext: Button
    private lateinit var recyclerView: RecyclerView
    private val predictedResults = mutableSetOf<String>() // To store selected equipment predictions

    // Using ActivityResultLauncher for image selection
    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                imageView.setImageBitmap(bitmap)
            }
        }

    // Using ActivityResultLauncher for taking picture with camera
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                imageView.setImageURI(photoUri)
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

    private lateinit var photoUri: Uri

    // Check and request camera permission at runtime
    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, launch the camera
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun openCamera() {
        // Create a content URI to store the captured image
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "Captured Image")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        // Launch the camera to capture a photo
        takePictureLauncher.launch(photoUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_equipment)

        imageView = findViewById(R.id.imageView_preview)
        buttonSelectImage = findViewById(R.id.button_select_image)
        buttonOpenCamera = findViewById(R.id.button_open_camera)  // Initialize the camera button
        buttonPredict = findViewById(R.id.button_predict)
        buttonNext = findViewById(R.id.button_next) // Next button
        recyclerView = findViewById(R.id.recyclerView_equipment) // RecyclerView

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = EquipmentAdapter(this, getEquipmentList(), predictedResults.toList())
        recyclerView.adapter = adapter

        // Initialize the TensorFlow Lite interpreter
        interpreter = Interpreter(loadModelFile())

        buttonSelectImage.setOnClickListener {
            // Open gallery to pick image using ActivityResultLauncher
            getImageLauncher.launch("image/*")
        }

        buttonOpenCamera.setOnClickListener {
            // Check if the camera permission is granted
            checkAndRequestCameraPermission()
        }

        buttonPredict.setOnClickListener {
            // Make prediction based on selected image
            val drawable = imageView.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val result = predict(bitmap)

                // Add the prediction to the set
                predictedResults.addAll(result)

                // Update the UI with the result
                updateUIWithPrediction(predictedResults.toList())
            } else {
                // Show a toast if no image is selected
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }

        buttonNext.setOnClickListener {
            // Navigate to ResultActivity
            val intent = Intent(this, ResultEquipmentActivity::class.java)
            startActivity(intent)
        }

        // Disable the 'Next' button initially
        buttonNext.isEnabled = false
    }

    private fun loadModelFile(): ByteBuffer {
        val assetManager = assets
        val fileDescriptor = assetManager.openFd("equipments_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.length
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun predict(bitmap: Bitmap): List<String> {
        val input = preprocessImage(bitmap)
        val output = Array(1) { FloatArray(5) } // 5 classes (Diving Fins, Mask, Oxygen Tank, Regulators, Wetsuit)
        interpreter.run(input, output)

        // Log output untuk memastikan prediksi
        println("Model output: ${output[0].joinToString(", ")}")  // Tambahkan log untuk melihat hasil output

        val predictedLabels = mutableListOf<String>()

        // Update prediksi satu per satu, pastikan kondisi threshold diterapkan dengan benar
        output[0].forEachIndexed { index, value ->
            println("Output for ${getEquipmentLabel(index)}: $value")  // Log nilai prediksi untuk tiap kategori
            if (value > 0.5) {  // Threshold untuk klasifikasi
                val label = getEquipmentLabel(index)
                predictedLabels.add(label)
                println("Added $label to predicted labels")
            }
        }

        return predictedLabels
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        // Preprocess the image (resize, normalize) as required by your model
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val buffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3) // For RGB 224x224 image
        buffer.order(ByteOrder.nativeOrder())

        // Normalize and put pixel values into ByteBuffer
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = resizedBitmap.getPixel(x, y)
                buffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f) // Red
                buffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)  // Green
                buffer.putFloat((pixel and 0xFF) / 255.0f)          // Blue
            }
        }
        return buffer
    }

    private fun getEquipmentLabel(index: Int): String {
        return when (index) {
            0 -> "Diving Fins"
            1 -> "Diving Mask"
            2 -> "Oxygen Tank"
            3 -> "Diving Regulators"
            4 -> "Diving Wetsuit"
            else -> "Unknown"
        }
    }

    private fun updateUIWithPrediction(predictedLabels: List<String>) {
        // Log untuk mengecek apakah Oxygen Tank ada dalam predictedLabels
        println("Predicted labels: $predictedLabels")

        val adapter = EquipmentAdapter(this, getEquipmentList(), predictedLabels)
        recyclerView.adapter = adapter

        // Aktifkan tombol Next hanya jika semua prediksi relevan tercentang
        buttonNext.isEnabled = getEquipmentList().all { predictedLabels.contains(it) }
    }

    private fun getEquipmentList(): List<String> {
        return listOf("Diving Fins", "Diving Mask", "Oxygen Tank", "Diving Regulators", "Diving Wetsuit")
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
