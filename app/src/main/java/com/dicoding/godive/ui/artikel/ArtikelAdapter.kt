package com.dicoding.godive.ui.artikel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.godive.R
import com.dicoding.godive.data.remote.response.DivingPlaceItem


class ArtikelAdapter(private val context: Context, private val listArtikel: List<DivingPlaceItem>) : RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_artikel, parent, false)
        return ArtikelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val item = listArtikel[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listArtikel.size

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.divePlaceImage)
        private val nameTextView: TextView = itemView.findViewById(R.id.divePlaceName)
        private val locationTextView: TextView = itemView.findViewById(R.id.divePlaceLocation)

        fun bind(divingPlaceItem: DivingPlaceItem) {
            nameTextView.text = divingPlaceItem.nama
            locationTextView.text = divingPlaceItem.lokasi
            Glide.with(itemView.context).load(divingPlaceItem.image).into(imageView)

            itemView.setOnClickListener {
                // Intent untuk membuka DetailArtikel dan mengirim data divingPlaceItem
                val intent = Intent(itemView.context, DetailArtikelActivity::class.java).apply {
                    putExtra("divingPlace", divingPlaceItem)  // Passing Parcelable object
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}

