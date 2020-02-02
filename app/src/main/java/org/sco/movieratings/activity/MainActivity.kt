package org.sco.movieratings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.sco.movieratings.R

private const val TAG = "MainActivity"
private const val SORT_MODE = "sortMode"
class MainActivity : AppCompatActivity() {

    private var sort: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (savedInstanceState != null) {
            sort = savedInstanceState.getString(
                SORT_MODE,
                getString(R.string.pref_sort_top_rated)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SORT_MODE, sort)
    }

    fun setTitle(title: String) {
        supportActionBar?.title = "${getText(R.string.app_name)} $title"
    }

}