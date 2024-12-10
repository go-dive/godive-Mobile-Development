package com.dicoding.godive.data.fish

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.channels.FileChannel

class ImageClassifier(context: Context) {

    private val interpreter: Interpreter

    init {
        val model = loadModelFile(context)
        interpreter = Interpreter(model)
    }

    // Function to load model file
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd("species_model.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Function to preprocess image and classify
    fun classifyImage(bitmap: Bitmap): Float {
        val inputSize = 224  // Sesuaikan ukuran input sesuai dengan model Anda
        val byteBuffer = preprocessImage(bitmap, inputSize)

        val output = Array(1) { FloatArray(2) }  // Output untuk 2 kelas (danger atau safe)

        // Memanggil model untuk prediksi
        interpreter.run(byteBuffer, output)

        return output[0][0]  // Mengembalikan nilai prediksi (misalnya, probabilitas "danger")
    }

    // Function to convert bitmap to ByteBuffer for the model input
    private fun preprocessImage(bitmap: Bitmap, inputSize: Int): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                val px = scaledBitmap.getPixel(x, y)

                byteBuffer.putFloat(((px shr 16) and 0xFF) / 255.0f)  // Red
                byteBuffer.putFloat(((px shr 8) and 0xFF) / 255.0f)   // Green
                byteBuffer.putFloat((px and 0xFF) / 255.0f)           // Blue
            }
        }

        return byteBuffer
    }

    // Release interpreter when done
    fun close() {
        interpreter.close()
    }
}
