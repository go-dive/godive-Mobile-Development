package com.dicoding.godive.ui.health

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.godive.R
import com.dicoding.godive.databinding.ActivityHealthBinding

class HealthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHealthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, QuestionHealthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}