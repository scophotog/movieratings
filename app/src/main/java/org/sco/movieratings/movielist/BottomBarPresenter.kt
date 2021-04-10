package org.sco.movieratings.movielist

import com.google.android.material.bottomnavigation.BottomNavigationView
import org.sco.movieratings.databinding.FragmentMovieListBinding

class BottomBarPresenter(private val binding: FragmentMovieListBinding) {

    fun setSelectedItemById(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }

    fun setOnNavigationItemSelectedListener(listener: BottomNavigationView.OnNavigationItemSelectedListener) {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(listener)
    }
}