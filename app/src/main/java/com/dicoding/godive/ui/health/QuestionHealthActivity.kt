package com.dicoding.godive.ui.health

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.godive.R

class QuestionHealthActivity : AppCompatActivity() {
    private val answers = FloatArray(10) // Array untuk menampung jawaban (10 pertanyaan)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_health)

        findViewById<Button>(R.id.btnProcess).setOnClickListener {
            // Capture answers
            val radioGroups = arrayOf(
                findViewById<RadioGroup>(R.id.radio_group_1),
                findViewById<RadioGroup>(R.id.radio_group_2),
                findViewById<RadioGroup>(R.id.radio_group_3),
                findViewById<RadioGroup>(R.id.radio_group_4),
                findViewById<RadioGroup>(R.id.radio_group_5),
                findViewById<RadioGroup>(R.id.radio_group_6),
                findViewById<RadioGroup>(R.id.radio_group_7),
                findViewById<RadioGroup>(R.id.radio_group_8),
                findViewById<RadioGroup>(R.id.radio_group_9),
                findViewById<RadioGroup>(R.id.radio_group_10)
            )

            var yesCount = 0
            var noCount = 0

            for (i in radioGroups.indices) {
                answers[i] = if (radioGroups[i].checkedRadioButtonId == R.id.yes_1) 1f else 0f
                if (answers[i] == 1f) yesCount++ else noCount++
            }

            // Tentukan hasil berdasarkan jumlah 'Yes' dan 'No'
            val resultText = when {
                yesCount == 10 -> "Anda Tidak Siap Diving"  // Semua "Yes"
                noCount == 10 -> "Anda Siap Diving"  // Semua "No"
                else -> {
                    val probability = yesCount.toFloat() / 10  // Hitung probabilitas
                    if (probability > 0.5) "Anda Siap Diving" else "Anda Tidak Siap Diving"
                }
            }

            // Kirim hasil ke ResultQuestionActivity
            val intent = Intent(this, ResultQuestionActivity::class.java)
            intent.putExtra("predictionResult", resultText)
            startActivity(intent)
        }
    }
}
