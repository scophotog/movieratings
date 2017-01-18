package org.sco.movieratings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
}
