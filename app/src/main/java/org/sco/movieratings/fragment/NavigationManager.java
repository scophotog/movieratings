package org.sco.movieratings.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by sargenzi on 1/20/17.
 */

public class NavigationManager {

    private final FragmentManager fragmentManager;

    public NavigationManager(@NonNull FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void navigateTo(@NonNull Fragment fragment, @IdRes int inLayoutResId) {
        final String tag = fragment.getClass().getName();

        fragmentManager.beginTransaction()
                .replace(inLayoutResId, fragment, tag)
                .commit();

    }

    public void navigateToActivity(@NonNull Activity activity, @NonNull Context context) {
        Intent intent = activity.getIntent();
        context.startActivity(intent);
    }

}
