package com.dicoding.godive.data.artikel.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.godive.data.artikel.data.dao.FavoriteArticleDao
import com.dicoding.godive.data.artikel.data.model.FavoriteArticle

@Database(entities = [FavoriteArticle::class], version = 1)
abstract class FavoriteArticleDatabase : RoomDatabase() {

    abstract fun favoriteArticleDao(): FavoriteArticleDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteArticleDatabase? = null

        fun getDatabase(context: Context): FavoriteArticleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteArticleDatabase::class.java,
                    "favorite_article_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
