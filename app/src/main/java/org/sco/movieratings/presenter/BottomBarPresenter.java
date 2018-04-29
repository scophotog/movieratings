package org.sco.movieratings.presenter;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.View;

import org.sco.movieratings.R;

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
