package com.dicoding.godive.ui.health

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.godive.R

class ResultQuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_question)

        // Ambil hasil prediksi dari Intent
        val resultText = intent.getStringExtra("predictionResult") ?: "Hasil tidak valid"

        // Tampilkan hasil di TextView
        findViewById<TextView>(R.id.resultText).text = resultText

        // Tangani klik tombol kembali
        findViewById<Button>(R.id.btnGoBack).setOnClickListener {
            finish()  // Close the activity
        }
    }
}