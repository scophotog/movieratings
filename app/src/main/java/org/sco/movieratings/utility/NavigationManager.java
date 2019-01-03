package org.sco.movieratings.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
