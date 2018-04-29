package org.sco.movieratings.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.sco.movieratings.R;

/**
 * Created by sargenzi on 1/10/17.
 */

public final class Utility {

    private Utility(){}

    public static String getPreferredSort(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sortType = prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_top_rated));
        return sortType;
    }

    public static void updatePreference(final Context context, final String sortType) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(context.getString(R.string.pref_sort_key), sortType).apply();
    }
}
