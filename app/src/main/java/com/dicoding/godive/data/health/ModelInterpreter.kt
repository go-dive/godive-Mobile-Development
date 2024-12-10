import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ModelInterpreter(private val context: Context) {
    private var interpreter: Interpreter? = null

    init {
        try {
            val model = loadModelFile(context)
            interpreter = Interpreter(model)
            if (interpreter == null) {
                Log.e("ModelInterpreter", "Interpreter initialization failed")
            }
        } catch (e: IOException) {
            Log.e("ModelInterpreter", "Model loading failed: ${e.message}")
            e.printStackTrace()
        }
    }

    // Memuat model TensorFlow Lite dari file assets
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("model.tflite")
        val inputStream = fileDescriptor.createInputStream()
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Metode untuk memprediksi menggunakan model
    fun predict(input: FloatArray): Float {
        // Membuat tensor input untuk model
        val inputTensor = arrayOf(input)

        // Membuat buffer untuk output
        val output = Array(1) { FloatArray(1) }

        try {
            // Melakukan prediksi
            interpreter?.run(inputTensor, output)

            // Mengambil hasil output dari model
            return output[0][0] // Output berupa probabilitas (misal: 0.7)
        } catch (e: Exception) {
            Log.e("ModelInterpreter", "Prediction failed: ${e.message}")
            return -1f
        }
    }

}
