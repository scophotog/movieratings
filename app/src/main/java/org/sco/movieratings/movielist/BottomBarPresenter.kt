package org.sco.movieratings.movielist

import com.google.android.material.navigation.NavigationBarView
import org.sco.movieratings.R
import org.sco.movieratings.databinding.FragmentMovieListBinding

class BottomBarPresenter(private val binding: FragmentMovieListBinding) {

    private val resources = binding.root.resources

    fun setViewToSortType(sortType: String) {
        val topRated = resources.getString(R.string.pref_sort_top_rated)
        val popular = resources.getString(R.string.pref_sort_popular_rated)
        val favorites = resources.getString(R.string.pref_sort_my_favorites)
        when (sortType) {
            topRated -> {
                setSelectedItemById(R.id.bn_top_rated)
            }
            popular -> {
                setSelectedItemById(R.id.bn_most_popular)
            }
            favorites -> {
                setSelectedItemById(R.id.bn_my_favorites)
            }
            else -> {
                throw IllegalArgumentException("Unknown navigation: $sortType")
            }
        }
    }

    fun setOnItemSelectedListener(listener: NavigationBarView.OnItemSelectedListener) {
        binding.bottomNavigation.setOnItemSelectedListener(listener)
    }

    private fun setSelectedItemById(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }
}