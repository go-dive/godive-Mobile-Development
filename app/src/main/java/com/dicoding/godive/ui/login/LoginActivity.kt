package com.dicoding.godive.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.godive.MainActivity
import com.dicoding.godive.data.login.LoginRequest
import com.dicoding.godive.data.login.response.LoginResponse
import com.dicoding.godive.data.login.retrofite.RetrofitInstancee
import com.dicoding.godive.databinding.ActivityLoginBinding
import com.dicoding.godive.ui.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button Login
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Button Register (ke RegisterActivity)
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val loginRequest = LoginRequest(email, password)
            val response: Response<LoginResponse> = RetrofitInstancee.api.login(loginRequest)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val token = response.body()?.user?.token
                    val message = response.body()?.message
                    Toast.makeText(this@LoginActivity, message ?: "Login successful", Toast.LENGTH_SHORT).show()

                    // Pastikan token tidak null dan valid
                    if (token != null && token.isNotEmpty()) {
                        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("TOKEN", token)  // Simpan token
                            apply()  // Simpan perubahan
                        }
                        // Pindah ke halaman utama setelah login berhasil
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Failed to receive valid token.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Jika login gagal, tampilkan pesan kesalahan
                    val errorMessage = response.body()?.message ?: "Login failed. Please try again."
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
