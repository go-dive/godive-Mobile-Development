package com.dicoding.godive.ui.equipment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.godive.R
import com.dicoding.godive.ui.equipment.PredictEquipmentActivity

class EquipmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_equipment)

        // Set up onClickListener for the Next button
        val btnNext = findViewById<View>(R.id.btnNext)
        btnNext.setOnClickListener {
            // Navigate to PredictFishActivity when button is clicked
            val intent = Intent(this, PredictEquipmentActivity::class.java)
            startActivity(intent)
        }

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
