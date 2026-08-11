package de.baumann.browser.fragment;


import android.os.Bundle;

import de.baumann.browser.R;
import de.baumann.browser.preferences.BasePreferenceFragment;

public class Fragment_settings_Delete extends BasePreferenceFragment {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_delete, rootKey);
    }
}