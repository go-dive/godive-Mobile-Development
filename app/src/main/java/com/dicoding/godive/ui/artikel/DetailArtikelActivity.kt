package com.dicoding.godive.ui.artikel

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.godive.R
import com.dicoding.godive.data.artikel.data.database.FavoriteArticleDatabase
import com.dicoding.godive.data.artikel.data.model.FavoriteArticle
import com.dicoding.godive.data.remote.response.DivingPlaceItem
import kotlinx.coroutines.launch

class DetailArtikelActivity : AppCompatActivity() {

    private var isFavorite = false  // Menyimpan status apakah artikel sudah di favoritkan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        val divingPlace = intent.getParcelableExtra<DivingPlaceItem>("divingPlace")

        divingPlace?.let {
            // Menampilkan data di UI
            findViewById<TextView>(R.id.detailNama).text = it.nama
            findViewById<TextView>(R.id.detailLokasi).text = it.lokasi
            findViewById<TextView>(R.id.detailDeskripsi).text = it.deskripsi
            findViewById<TextView>(R.id.detailRating).text = "Rating: ${it.rating}"

            Glide.with(this).load(it.image).into(findViewById<ImageView>(R.id.detailImage))

            // Cek status favorit dan sesuaikan ikon
            checkFavoriteStatus(it)
        }

        // Tombol untuk kembali ke halaman sebelumnya
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Tombol untuk menambah atau menghapus favorit
        val favoriteButton = findViewById<ImageView>(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            divingPlace?.let { place ->
                if (isFavorite) {
                    removeFavorite(place)
                } else {
                    addFavorite(place)
                }
            }
        }
    }

    // Menambahkan artikel ke favorit
    private fun addFavorite(divingPlace: DivingPlaceItem) {
        lifecycleScope.launch {
            val db = FavoriteArticleDatabase.getDatabase(applicationContext)
            val favoriteArticle = FavoriteArticle(
                name = divingPlace.nama ?: "",
                location = divingPlace.lokasi ?: "",
                description = divingPlace.deskripsi ?: "",
                rating = divingPlace.rating?.toString() ?: "",
                image = divingPlace.image ?: ""
            )

            // Memeriksa apakah artikel sudah ada di database
            val existingFavorite = db.favoriteArticleDao().getFavoriteByName(favoriteArticle.name)
            if (existingFavorite == null) {
                // Jika artikel belum ada, tambahkan ke database
                db.favoriteArticleDao().insert(favoriteArticle)
                isFavorite = true
                findViewById<ImageView>(R.id.favoriteButton).setImageResource(R.drawable.ic_favorite_red)
            }
        }
    }

    // Menghapus artikel dari favorit
    private fun removeFavorite(divingPlace: DivingPlaceItem) {
        lifecycleScope.launch {
            val db = FavoriteArticleDatabase.getDatabase(applicationContext)
            val favoriteArticle = FavoriteArticle(
                name = divingPlace.nama ?: "",
                location = divingPlace.lokasi ?: "",
                description = divingPlace.deskripsi ?: "",
                rating = divingPlace.rating?.toString() ?: "",
                image = divingPlace.image ?: ""
            )

            // Menghapus artikel dari database
            db.favoriteArticleDao().delete(favoriteArticle)
            isFavorite = false
            findViewById<ImageView>(R.id.favoriteButton).setImageResource(R.drawable.ic_favorite)
        }
    }

    // Mengecek status favorit artikel
    private fun checkFavoriteStatus(divingPlace: DivingPlaceItem) {
        lifecycleScope.launch {
            val db = FavoriteArticleDatabase.getDatabase(applicationContext)
            val favoriteArticle = db.favoriteArticleDao().getFavoriteByName(divingPlace.nama ?: "")
            isFavorite = favoriteArticle != null
            findViewById<ImageView>(R.id.favoriteButton).setImageResource(
                if (isFavorite) R.drawable.ic_favorite_red else R.drawable.ic_favorite
            )
        }
    }
}

