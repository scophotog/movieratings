package org.sco.movieratings.utility

import android.content.Context
import androidx.preference.PreferenceManager
import org.sco.movieratings.R

object Utility {
    @JvmStatic
    fun getPreferredSort(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(
            context.getString(R.string.pref_sort_key),
            context.getString(R.string.pref_sort_top_rated)
        )
    }

    @JvmStatic
    fun updatePreference(context: Context, sortType: String?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putString(context.getString(R.string.pref_sort_key), sortType).apply()
    }
}