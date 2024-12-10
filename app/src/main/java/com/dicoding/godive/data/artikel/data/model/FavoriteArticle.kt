package com.dicoding.godive.data.artikel.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicoding.godive.data.remote.response.DivingPlaceItem
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite_articles")
data class FavoriteArticle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "rating") val rating: String,
    @ColumnInfo(name = "image") val image: String
) : Parcelable {

    // Fungsi konversi FavoriteArticle ke DivingPlaceItem
    fun toDivingPlaceItem(): DivingPlaceItem {
        return DivingPlaceItem(
            nama = this.name,
            lokasi = this.location,
            deskripsi = this.description,
            rating = this.rating.toDoubleOrNull() ?: 0.0,  // Pastikan rating menjadi Double
            image = this.image
        )
    }
}


