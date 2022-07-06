package org.sco.movieratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.sco.movieratings.databinding.FragmentViewPagerBinding

@AndroidEntryPoint
class MovieViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = MoviePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        arguments?.get("tab")?.let {
            if (it == "favorite") {
                viewPager.setCurrentItem(2, true)
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            MOVIE_LIST_POPULAR -> getString(R.string.most_popular_settings)
            MOVIE_LIST_TOP -> getString(R.string.high_rated_settings)
            MOVIE_LIST_FAVORITE -> getString(R.string.my_favorites_settings)
            else -> null
        }
    }
}