package org.sco.movieratings.presenter;

import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.sco.movieratings.R;

import androidx.annotation.NonNull;

public class BottomBarPresenter {

    private BottomNavigationView mBottomNavigationView;

    public BottomBarPresenter(@NonNull View view) {
        mBottomNavigationView = view.findViewById(R.id.bottom_navigation);
            }

    public void setSelectedItemById(int itemId) {
        mBottomNavigationView.setSelectedItemId(itemId);
    }

    public void setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener listener) {
        mBottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }
}
