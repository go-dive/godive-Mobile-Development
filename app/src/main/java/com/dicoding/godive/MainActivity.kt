package com.dicoding.godive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.godive.ui.artikel.ArtikelFragment
import com.dicoding.godive.ui.cuaca.CuacaFragment
import com.dicoding.godive.ui.favorite.FavoriteFragment
import com.dicoding.godive.ui.home.HomeFragment
import com.dicoding.godive.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home ->{
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_cuaca ->{
                    replaceFragment(CuacaFragment())
                    true
                }
                R.id.bottom_artikel ->{
                    replaceFragment(ArtikelFragment())
                    true
                }
                R.id.bottom_favorite ->{
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.bottom_profile ->{
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}