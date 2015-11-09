package au.com.fintechapps.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import java.util.Objects;

/**
 * Created by Admin on 14/09/2015.
 */
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        // Hide the requery data key from options as it for internal use only
        // I guess no real reason why the user could have this as an option to change. Consider
        PreferenceScreen screen = getPreferenceScreen();
        Preference pref = getPreferenceManager().findPreference(getString(R.string.requery_data_key));
        screen.removePreference(pref);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Don't want to create a logic loop! Only when the sort key is changed.
        // Otherwise we are just flagging our requery key true or false
        if (!(key.equals(getString(R.string.requery_data_key)))) {
            setRequeryDataFlag();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

    }

    //Key to decide if we requery that data or just use the objects stored in the view tag.
    public void setRequeryDataFlag() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(getString(R.string.requery_data_key), true);
        edit.apply();
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        //Toast.makeText(getApplicationContext(), "click",2000).show();
        String cameback="CameBack";
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Comingback", cameback);
        startActivity(intent);
    }
}


