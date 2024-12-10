package com.dicoding.godive.ui.home

import android.content.Intent // Import Intent untuk navigasi ke Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.godive.R
import com.dicoding.godive.data.remote.api.ApiService
import com.dicoding.godive.data.remote.response.DivingPlaceItem
import com.dicoding.godive.ui.artikel.ArtikelAdapter
import com.dicoding.godive.ui.equipment.EquipmentActivity
import com.dicoding.godive.ui.fish.FishActivity
import com.dicoding.godive.ui.health.HealthActivity // Import HealthActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var imageSlider: ViewPager2
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable
    private val images = listOf(
        R.drawable.rajaampat1,
        R.drawable.rajaampat2,
        R.drawable.rajaampat3,
        R.drawable.aba1,
        R.drawable.aba2,
        R.drawable.aba3,
        R.drawable.aba4
    )

    // Declare card views
    private lateinit var healthCard: CardView
    private lateinit var equipmentCard: CardView
    private lateinit var fishCard: CardView

    // Dots indicator
    private lateinit var dotIndicator: LinearLayout
    private lateinit var dotViews: MutableList<ImageView>

    // RecyclerView untuk menampilkan artikel
    private lateinit var artikelAdapter: ArtikelAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menginisialisasi image slider
        imageSlider = view.findViewById(R.id.imageSlider)
        val adapter = ImageSliderAdapter(images)
        imageSlider.adapter = adapter

        // Inisialisasi dot indicator
        dotIndicator = view.findViewById(R.id.dotIndicator)
        dotViews = mutableListOf()
        addDotIndicators()

        // Mulai dari tengah untuk meminimalkan efek looping terlihat
        val startPosition = Int.MAX_VALUE / 2
        imageSlider.setCurrentItem(startPosition - (startPosition % images.size), false)

        // Autoplay logic
        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = Runnable {
            imageSlider.currentItem = imageSlider.currentItem + 1
        }

        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDotIndicators(position % images.size) // Update dots when page changes
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        // Inisialisasi card views
        healthCard = view.findViewById(R.id.card_health)
        equipmentCard = view.findViewById(R.id.card_equipment)
        fishCard = view.findViewById(R.id.card_fish)

        // Inisialisasi RecyclerView dan adapter untuk artikel
        recyclerView = view.findViewById(R.id.recyclerViewHome)
        artikelAdapter = ArtikelAdapter(requireContext(), emptyList())  // Menggunakan requireContext() untuk menginisialisasi adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = artikelAdapter

        // Ambil data tempat diving dari API
        fetchDivingPlaces()

        // Set OnClickListener untuk health card
        healthCard.setOnClickListener {
            // Navigasi ke HealthActivity
            val intent = Intent(requireContext(), HealthActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener untuk equipment card
        equipmentCard.setOnClickListener {
            // Navigasi ke EquipmentActivity
            val intent = Intent(requireContext(), EquipmentActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener untuk fish card
        fishCard.setOnClickListener {
            // Navigasi ke FishActivity
            val intent = Intent(requireContext(), FishActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addDotIndicators() {
        for (i in images.indices) {
            val dot = ImageView(requireContext())
            val params = LinearLayout.LayoutParams(10.dpToPx(), 10.dpToPx()).apply {
                setMargins(6.dpToPx(), 0, 6.dpToPx(), 0)
            }
            dot.layoutParams = params
            dot.setImageResource(R.drawable.inactive_dot)
            dotIndicator.addView(dot)
            dotViews.add(dot)
        }
    }

    private fun updateDotIndicators(position: Int) {
        for (i in dotViews.indices) {
            val dot = dotViews[i]
            if (i == position) {
                dot.setImageResource(R.drawable.active_dot)
            } else {
                dot.setImageResource(R.drawable.inactive_dot)
            }
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    // Fungsi untuk fetch data diving places
    private fun fetchDivingPlaces() {
        ApiService.create().getDivingPlaces().enqueue(object : Callback<List<DivingPlaceItem>> {
            override fun onResponse(
                call: Call<List<DivingPlaceItem>>,
                response: Response<List<DivingPlaceItem>>
            ) {
                if (response.isSuccessful) {
                    val divingPlaceList = response.body() ?: emptyList()
                    artikelAdapter = ArtikelAdapter(requireContext(), divingPlaceList)  // Pastikan memberikan konteks yang benar
                    recyclerView.adapter = artikelAdapter
                    artikelAdapter.notifyDataSetChanged()
                } else {
                    Log.e("HomeFragment", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<DivingPlaceItem>>, t: Throwable) {
                Log.e("HomeFragment", "API request failed", t)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacks(sliderRunnable)
    }
}
