package org.ligboy.preference.demo;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.ligboy.preference.PreferenceFragmentCompat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsCompatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsCompatFragment extends PreferenceFragmentCompat {

    public static SettingsCompatFragment newInstance() {
        return new SettingsCompatFragment();
    }

    public SettingsCompatFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_main);
    }

}
