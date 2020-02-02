package org.sco.movieratings.utility

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class NavigationManager(private val fragmentManager: FragmentManager) {
    fun navigateTo(fragment: Fragment, @IdRes inLayoutResId: Int) {
        val tag = fragment.javaClass.name
        fragmentManager.beginTransaction()
            .replace(inLayoutResId, fragment, tag)
            .commit()
    }

    fun navigateToActivity(activity: Activity, context: Context) {
        val intent = activity.intent
        context.startActivity(intent)
    }

}