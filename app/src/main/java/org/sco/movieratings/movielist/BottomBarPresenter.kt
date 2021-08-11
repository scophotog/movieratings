package org.sco.movieratings.movielist

import com.google.android.material.navigation.NavigationBarView
import org.sco.movieratings.databinding.FragmentMovieListBinding

class BottomBarPresenter(private val binding: FragmentMovieListBinding) {

    fun setSelectedItemById(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }

    fun setOnItemSelectedListener(listener: NavigationBarView.OnItemSelectedListener) {
        binding.bottomNavigation.setOnItemSelectedListener(listener)
    }
}