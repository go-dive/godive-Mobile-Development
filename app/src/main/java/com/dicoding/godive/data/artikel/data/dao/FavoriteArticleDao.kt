package com.dicoding.godive.data.artikel.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.godive.data.artikel.data.model.FavoriteArticle

@Dao
interface FavoriteArticleDao {

    @Insert
    suspend fun insert(favoriteArticle: FavoriteArticle)

    @Delete
    suspend fun delete(favoriteArticle: FavoriteArticle)

    @Query("SELECT * FROM favorite_articles WHERE name = :name LIMIT 1")  // Menambahkan query untuk mencari berdasarkan nama
    suspend fun getFavoriteByName(name: String): FavoriteArticle?

    @Query("SELECT * FROM favorite_articles")  // Menyesuaikan dengan nama tabel yang Anda gunakan
    fun getAllFavorites(): LiveData<List<FavoriteArticle>>
}

