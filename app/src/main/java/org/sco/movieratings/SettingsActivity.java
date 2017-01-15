package org.sco.movieratings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import org.sco.movieratings.R;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private AppCompatDelegate mDelegate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        loadToolbar();

        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == android.R.id.home) {
           this.finish();
           return true;
       }

        return super.onOptionsItemSelected(item);
    }

    private void loadToolbar() {
        getDelegate().setContentView(R.layout.activity_settings);
        getDelegate().setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }

        return true;
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
}
