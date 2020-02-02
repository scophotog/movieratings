package org.sco.movieratings.presenter

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.sco.movieratings.R

class BottomBarPresenter(val view: View) {

    private val bottomNavigationView: BottomNavigationView by lazy {
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
    }

    fun setSelectedItemById(itemId: Int) {
        bottomNavigationView.selectedItemId = itemId
    }

    fun setOnNavigationItemSelectedListener(listener: BottomNavigationView.OnNavigationItemSelectedListener) {
        bottomNavigationView.setOnNavigationItemSelectedListener(listener)
    }
}