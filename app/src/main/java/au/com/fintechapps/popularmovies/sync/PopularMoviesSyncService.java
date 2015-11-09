package au.com.fintechapps.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Objects;

/**
 * Created by Admin on 26/10/2015.
 */
public class PopularMoviesSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static PopularMoviesSyncAdapter sPopularMoviesSyncAdapter = null;


    @Override
    public void onCreate(){
        Log.v("popular","Oncreate sync service");

        synchronized (sSyncAdapterLock){
                if (sPopularMoviesSyncAdapter==null) {
                    sPopularMoviesSyncAdapter = new PopularMoviesSyncAdapter(getApplicationContext(),true);
                }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return sPopularMoviesSyncAdapter.getSyncAdapterBinder();
    }
}
