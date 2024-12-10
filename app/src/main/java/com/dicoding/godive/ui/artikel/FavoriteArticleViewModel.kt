package com.dicoding.godive.ui.artikel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.godive.data.artikel.data.database.FavoriteArticleDatabase
import com.dicoding.godive.data.artikel.data.model.FavoriteArticle
import kotlinx.coroutines.launch

class FavoriteArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteArticleDao = FavoriteArticleDatabase.getDatabase(application).favoriteArticleDao()
    val allFavorites: LiveData<List<FavoriteArticle>> = favoriteArticleDao.getAllFavorites()

    fun addFavorite(article: FavoriteArticle) {
        viewModelScope.launch {
            favoriteArticleDao.insert(article)
        }
    }

    fun removeFavorite(article: FavoriteArticle) {
        viewModelScope.launch {
            favoriteArticleDao.delete(article)
        }
    }
}
