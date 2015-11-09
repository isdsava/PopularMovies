package au.com.fintechapps.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Admin on 26/10/2015.
 */
public class PopularMoviesAuthenticatorService extends Service {

    private PopularMoviesAuthenticator mMoviesAuthenticator;

    @Override
    public void onCreate(){

        mMoviesAuthenticator = new PopularMoviesAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mMoviesAuthenticator.getIBinder();
    }
}
