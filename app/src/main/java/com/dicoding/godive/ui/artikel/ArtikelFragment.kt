package com.dicoding.godive.ui.artikel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.godive.databinding.FragmentArtikelBinding
import com.dicoding.godive.data.remote.api.ApiService
import com.dicoding.godive.data.remote.response.DivingPlaceItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtikelFragment : Fragment() {

    private lateinit var binding: FragmentArtikelBinding
    private lateinit var artikelAdapter: ArtikelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArtikelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set RecyclerView menggunakan GridLayoutManager
        artikelAdapter = ArtikelAdapter(requireContext(), emptyList()) // Mengirim context agar bisa membuat Intent
        binding.recyclerViewArtikel.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewArtikel.adapter = artikelAdapter

        // Ambil data dari API
        fetchArtikelData()
    }

    private fun fetchArtikelData() {
        ApiService.create().getDivingPlaces().enqueue(object : Callback<List<DivingPlaceItem>> {
            override fun onResponse(
                call: Call<List<DivingPlaceItem>>,
                response: Response<List<DivingPlaceItem>>
            ) {
                if (response.isSuccessful) {
                    val divingPlaceList = response.body() ?: emptyList()

                    // Update adapter dengan data dari API
                    artikelAdapter = ArtikelAdapter(requireContext(), divingPlaceList)
                    binding.recyclerViewArtikel.adapter = artikelAdapter
                    artikelAdapter.notifyDataSetChanged()
                } else {
                    Log.e("ArtikelFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<DivingPlaceItem>>, t: Throwable) {
                Log.e("ArtikelFragment", "API request failed", t)
            }
        })
    }
}
