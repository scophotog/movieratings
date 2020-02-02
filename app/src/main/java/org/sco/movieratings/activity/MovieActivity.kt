package org.sco.movieratings.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.sco.movieratings.R
import org.sco.movieratings.api.models.Movie
import org.sco.movieratings.fragment.MovieFragment
import org.sco.movieratings.utility.MovieListRouter

private const val LOG = "MovieActivity"

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        if (savedInstanceState == null) {
            val router = MovieListRouter(supportFragmentManager)
            router.startFragment((intent.getParcelableExtra<Parcelable>(MovieFragment.MOVIE) as Movie))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            movie: Movie?,
            context: Context?
        ): MovieActivity {
            val activity = MovieActivity()
            val intent = Intent(context, MovieActivity::class.java)
                .putExtra(MovieFragment.MOVIE, movie as Parcelable?)
            activity.intent = intent
            return activity
        }
    }
}