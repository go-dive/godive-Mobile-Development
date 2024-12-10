package com.dicoding.godive.ui.fish

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.godive.R

class ResultPredictActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_predict)

        val prediction = intent.getFloatExtra("prediction", -1f)
        val resultText = findViewById<TextView>(R.id.resultText)

        if (prediction == -1f) {
            resultText.text = "Gagal memprediksi gambar."
        } else {
            if (prediction > 0.5) {  // Misalkan jika nilai prediksi > 0.5 artinya "danger"
                resultText.text = "Hasil Prediksi: Dangerous Fish"
            } else {
                resultText.text = "Hasil Prediksi: Safe Fish"
            }
        }
        // Tangani klik tombol kembali
        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()  // Close the activity
        }
    }
}
