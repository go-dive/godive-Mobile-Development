package com.dicoding.godive.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.godive.MainActivity
import com.dicoding.godive.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tunggu selama 2 detik, kemudian cek status login
        Handler().postDelayed({
            // Periksa apakah token ada di SharedPreferences
            val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
            val token = sharedPref.getString("TOKEN", null)

            if (token != null) {
                // Jika token ada, buka halaman utama
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Jika token tidak ada, buka halaman login
                startActivity(Intent(this, LoginActivity::class.java))
            }

            // Tutup SplashActivity setelah pindah ke activity berikutnya
            finish()
        }, 2000) // 2 detik delay
    }
}