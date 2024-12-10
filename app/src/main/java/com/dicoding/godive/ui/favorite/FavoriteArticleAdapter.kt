package com.dicoding.godive.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.godive.R
import com.dicoding.godive.data.artikel.data.model.FavoriteArticle
import com.dicoding.godive.ui.artikel.DetailArtikelActivity

class FavoriteArticleAdapter(
    private var articles: List<FavoriteArticle>,
    private val onFavoriteClicked: (FavoriteArticle) -> Unit
) : RecyclerView.Adapter<FavoriteArticleAdapter.FavoriteArticleViewHolder>() {

    // Update data list dan beri tahu adapter untuk memperbarui UI
    fun setArticles(newArticles: List<FavoriteArticle>) {
        this.articles = newArticles
        notifyDataSetChanged() // Memberitahu adapter bahwa data telah berubah
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_article, parent, false)
        return FavoriteArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.nameTextView.text = article.name
        holder.locationTextView.text = article.location
        holder.ratingTextView.text = article.rating

        Glide.with(holder.itemView.context)
            .load(article.image)
            .into(holder.articleImageView)

        holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_red)
        holder.favoriteIcon.setOnClickListener { onFavoriteClicked(article) }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArtikelActivity::class.java).apply {
                putExtra("divingPlace", article.toDivingPlaceItem())  // Menggunakan fungsi konversi
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    class FavoriteArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.favoriteName)
        val locationTextView: TextView = view.findViewById(R.id.favoriteLocation)
        val ratingTextView: TextView = view.findViewById(R.id.favoriteRating)
        val articleImageView: ImageView = view.findViewById(R.id.favoriteImage)
        val favoriteIcon: ImageView = view.findViewById(R.id.favoriteIcon)
    }
}
