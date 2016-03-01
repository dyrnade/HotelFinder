package com.example.cem.hotel;

import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class HotelPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference deleteList = getPreferenceScreen().findPreference("pref_key_delete_list");

        deleteList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment dialog = new YesNoDialog();
                dialog.show(getFragmentManager(), "tag");
                return false;
            }
        });

    }

}
