package org.sco.movieratings

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.sco.movieratings.movielist.MovieListFragment
import org.sco.movieratings.movielist.MovieListType

const val MOVIE_LIST_POPULAR = 0
const val MOVIE_LIST_TOP = 1
const val MOVIE_LIST_FAVORITE = 2

class MoviePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        MOVIE_LIST_POPULAR to MovieListFragment.fragmentListInstance(MovieListType.POPULAR),
        MOVIE_LIST_TOP to MovieListFragment.fragmentListInstance(MovieListType.TOP),
        MOVIE_LIST_FAVORITE to MovieListFragment.fragmentListInstance(MovieListType.FAVORITE)
    )

    override fun getItemCount(): Int = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}