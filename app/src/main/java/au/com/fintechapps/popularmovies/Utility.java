package au.com.fintechapps.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 9/11/2015.
 */
public class Utility {

    public Utility(){}

    public boolean enableNotify(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_enable_notifications_key),
                true);
    }

    public boolean twoScreens;

    public String queryType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       return sharedPreferences.getString(context.getString(R.string.sort_key), context.getString(R.string.sort_default));
    }

    public  boolean refresh(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.requery_data_key), false);

    }
}
