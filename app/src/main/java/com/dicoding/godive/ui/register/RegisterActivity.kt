package com.dicoding.godive.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.godive.data.login.retrofite.RetrofitInstancee
import com.dicoding.godive.data.register.RegisterRequest
import com.dicoding.godive.data.register.response.RegisterResponse
import com.dicoding.godive.databinding.ActivityRegisterBinding
import com.dicoding.godive.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Button Register
        binding.registerBtn.setOnClickListener {
            val name = binding.emailEt.text.toString()  // Pastikan ID input nama sesuai
            val email = binding.normal.text.toString()
            val password = binding.passET.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(name, email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Button Login (ke LoginActivity)
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val registerRequest = RegisterRequest(email, password, name)
            val response: Response<RegisterResponse> = RetrofitInstancee.api.register(registerRequest)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val message = response.body()?.message
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()

                    // Ambil token dari response dan simpan di SharedPreferences
                    val token = response.body()?.user?.token
                    if (token != null) {
                        val sharedPref = getSharedPreferences("USER_PREF", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("TOKEN", token)  // Simpan token
                            apply()
                        }
                    }

                    // Pindah ke halaman login setelah registrasi berhasil
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@RegisterActivity, "Registration failed: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
