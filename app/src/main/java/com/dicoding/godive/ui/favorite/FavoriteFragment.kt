package com.dicoding.godive.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.godive.databinding.FragmentFavoriteBinding
import com.dicoding.godive.ui.artikel.FavoriteArticleViewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteArticleViewModel: FavoriteArticleViewModel
    private lateinit var adapter: FavoriteArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        // Inisialisasi ViewModel
        favoriteArticleViewModel = ViewModelProvider(this).get(FavoriteArticleViewModel::class.java)

        // Inisialisasi adapter
        adapter = FavoriteArticleAdapter(emptyList()) { article ->
            // Menghapus artikel dari favorit saat diklik
            favoriteArticleViewModel.removeFavorite(article)
        }

        // Set adapter ke RecyclerView
        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
        binding.rvFavorite.adapter = adapter

        // Observe perubahan data favorit
        favoriteArticleViewModel.allFavorites.observe(viewLifecycleOwner, { favorites ->
            adapter.setArticles(favorites)  // Update artikel setelah data berubah
        })

        return binding.root
    }
}
