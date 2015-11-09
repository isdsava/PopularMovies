package au.com.fintechapps.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UiAutomation;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import au.com.fintechapps.popularmovies.MainActivity;
import au.com.fintechapps.popularmovies.Movie.*;
import au.com.fintechapps.popularmovies.MovieFragment;
import au.com.fintechapps.popularmovies.MovieFragment.*;
import au.com.fintechapps.popularmovies.R;
import au.com.fintechapps.popularmovies.Utility;
import au.com.fintechapps.popularmovies.content.MovieColumns;
import au.com.fintechapps.popularmovies.content.MovieProvider;
import au.com.fintechapps.popularmovies.data.IApiMethods;
import au.com.fintechapps.popularmovies.data.MovieObject;
import au.com.fintechapps.popularmovies.data.Result;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Admin on 26/10/2015.
 */
public class PopularMoviesSyncAdapter  extends AbstractThreadedSyncAdapter{

    ContentResolver mContentResolver;
    //3hours to 90secs
    public static final int SYNC_INTERVAL = 180 * 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public PopularMoviesSyncAdapter (Context context,boolean autoInitialise){

        super(context,autoInitialise);

        mContentResolver = context.getContentResolver();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PopularMoviesSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {




        final String queryType = getContext().getString(R.string.sort_default);

        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";
        final String API_KEY = "6a51b1b7c06c245da2160c831051948e";
        Log.v("popular", "firedOnPerf");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IApiMethods iApiMethods = retrofit.create(IApiMethods.class);

        Call<MovieObject> call = iApiMethods.getMovies(API_KEY, queryType);

        //Make sure its online before we nuke everything
        if (isOnline()) {

            call.enqueue(new Callback<MovieObject>() {
                @Override
                public void onResponse(Response<MovieObject> response, Retrofit retrofit) {

                    ArrayList<Result> resultArrayList = new ArrayList<Result>();
                    resultArrayList = response.body().results;
                    Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(resultArrayList.size());

                    for (int i = 0; i < resultArrayList.size(); i++) {

                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieColumns.MOVIE_DB_ID, resultArrayList.get(i).id);
                        movieValues.put(MovieColumns.ORIGINAL_TITLE, resultArrayList.get(i).original_title);
                        movieValues.put(MovieColumns.TITLE, resultArrayList.get(i).title);
                        movieValues.put(MovieColumns.OVERVIEW, resultArrayList.get(i).overview);
                        movieValues.put(MovieColumns.POPULARITY, resultArrayList.get(i).popularity);
                        movieValues.put(MovieColumns.POSTER_PATH, resultArrayList.get(i).poster_path);
                        movieValues.put(MovieColumns.RELEASE_DATE, resultArrayList.get(i).release_date);
                        movieValues.put(MovieColumns.VIDEO, resultArrayList.get(i).video);
                        movieValues.put(MovieColumns.VOTE_AVERAGE, resultArrayList.get(i).vote_average);
                        movieValues.put(MovieColumns.VOTE_COUNT, resultArrayList.get(i).vote_count);
                        movieValues.put(MovieColumns.SOURCE_QUERY, queryType);
                        movieValues.put(MovieColumns.BACKDROP_PATH, resultArrayList.get(i).backdrop_path);

                        contentValuesVector.add(movieValues);
                    }

                    if (contentValuesVector.size() > 0) {
                        int delly = emptyTableMovies(queryType);
                        int isDone = 0;
                        ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
                        contentValuesVector.toArray(contentValuesArray);
                        isDone = getContext().getContentResolver().bulkInsert(MovieProvider.Movies.CONTENT_URI, contentValuesArray);
                        Log.v("popular", Integer.toString(isDone));
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    // Log error here since request failed
                    Log.v("popular", "ok dont panic but we have breached our britches a flood");
                }
            });

            final String queryTypeTwo = getContext().getString(R.string.sort_high_rate);
            Call<MovieObject> callTwo = iApiMethods.getMovies(API_KEY, queryTypeTwo);

            callTwo.enqueue(new Callback<MovieObject>() {
                @Override
                public void onResponse(Response<MovieObject> response, Retrofit retrofit) {

                    ArrayList<Result> resultArrayList = new ArrayList<Result>();
                    resultArrayList = response.body().results;
                    Vector<ContentValues> contentValuesVector = new Vector<ContentValues>(resultArrayList.size());

                    for (int i = 0; i < resultArrayList.size(); i++) {

                        ContentValues movieValues = new ContentValues();
                        movieValues.put(MovieColumns.MOVIE_DB_ID, resultArrayList.get(i).id);
                        movieValues.put(MovieColumns.ORIGINAL_TITLE, resultArrayList.get(i).original_title);
                        movieValues.put(MovieColumns.TITLE, resultArrayList.get(i).title);
                        movieValues.put(MovieColumns.OVERVIEW, resultArrayList.get(i).overview);
                        movieValues.put(MovieColumns.POPULARITY, resultArrayList.get(i).popularity);
                        movieValues.put(MovieColumns.POSTER_PATH, resultArrayList.get(i).poster_path);
                        movieValues.put(MovieColumns.RELEASE_DATE, resultArrayList.get(i).release_date);
                        movieValues.put(MovieColumns.VIDEO, resultArrayList.get(i).video);
                        movieValues.put(MovieColumns.VOTE_AVERAGE, resultArrayList.get(i).vote_average);
                        movieValues.put(MovieColumns.VOTE_COUNT, resultArrayList.get(i).vote_count);
                        movieValues.put(MovieColumns.SOURCE_QUERY, queryTypeTwo);
                        movieValues.put(MovieColumns.BACKDROP_PATH, resultArrayList.get(i).backdrop_path);

                        contentValuesVector.add(movieValues);
                    }

                    if (contentValuesVector.size() > 0) {
                        int delly = emptyTableMovies(queryTypeTwo);
                        int isDone = 0;
                        ContentValues[] contentValuesArray = new ContentValues[contentValuesVector.size()];
                        contentValuesVector.toArray(contentValuesArray);
                        isDone = getContext().getContentResolver().bulkInsert(MovieProvider.Movies.CONTENT_URI, contentValuesArray);
                        Log.v("popular", Integer.toString(isDone));
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    // Log error here since request failed
                    Log.v("popular", "ok dont panic but we have breached our britches a flood");
                }
            });

        }
        Utility   utility = new Utility();
        if (utility.enableNotify(getContext())){
                    testifyTheNotify((utility.enableNotify(getContext())));
        }
    }
    public static void initializeSyncAdapter(Context context) {
            getSyncAccount(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),MovieProvider.AUTHORITY, bundle);
       // SyncRequest syncRequest = new SyncRequest.Builder().syncOnce().build();
        //ContentResolver.requestSync(syncRequest);

        setPeriodicSyncUp(context);
    }


    public static void setPeriodicSyncUp(Context context){

        Account account = getSyncAccount(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            SyncRequest syncRequest = new SyncRequest.Builder().syncPeriodic(SYNC_INTERVAL,SYNC_FLEXTIME)
                                                            .setSyncAdapter(account,MovieProvider.AUTHORITY).
                                                            setExtras(new Bundle()).build();
            ContentResolver.requestSync(syncRequest);
        }
            else {
            ContentResolver.addPeriodicSync(account, MovieProvider.AUTHORITY, new Bundle(), SYNC_INTERVAL);

        }

            ContentResolver.setSyncAutomatically(account, MovieProvider.AUTHORITY, true);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            //onAccountCreated(newAccount, context);

        }
        return newAccount;
    }

    public int emptyTableMovies(String queryType){
        //cannons hoy out you go
        int cannonsHoy = 0;
        String selectionClause = MovieColumns.SOURCE_QUERY  + " = ?";
        String selection[] = {queryType};
        try {
            cannonsHoy = getContext().
                    getContentResolver().
                    delete(MovieProvider.Movies.CONTENT_URI,
                            selectionClause,
                            selection);

            Log.v("popular", "cannonshoy - " + Integer.toString(cannonsHoy));
        }
        catch (Exception e){

        }
        return cannonsHoy;
}

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    public void testifyTheNotify(boolean notify){

        if (notify){

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                                    .setContentTitle("Movies updated")
                                                                    .setContentText("Have a look you might like some of these");
            Intent movieIntent = new Intent(getContext(), MovieFragment.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());

                        stackBuilder.addParentStack(MainActivity.class);
                        stackBuilder.addNextIntent(movieIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                int id = 99;
            notificationManager.notify(id,builder.build());


        }
    }
}
