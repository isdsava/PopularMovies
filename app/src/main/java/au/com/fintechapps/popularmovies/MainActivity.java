package au.com.fintechapps.popularmovies;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.internal.Util;

import au.com.fintechapps.popularmovies.content.MovieProvider;
import au.com.fintechapps.popularmovies.sync.PopularMoviesSyncAdapter;
import au.com.fintechapps.popularmovies.Utility;

public class MainActivity extends ActionBarActivity implements MovieFragment.Callback{

    public boolean reRun = false;
    private boolean mTwoPane = false;
    private final String MOVIE_FRAG_TAG="MFTAG";
    private final String MOVIE_FRAG_TAG2="MFTAG2";
    private Uri mFirstLoadUri;
    private Utility utility = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_placeholder) !=null){
           utility.twoScreens = true;
                getSupportFragmentManager().beginTransaction()
                  .replace(R.id.movie_detail_placeholder,
                    MovieDetailFragment.newInstance(mFirstLoadUri),MOVIE_FRAG_TAG)
                    .addToBackStack(null)
                    .commit();

        }
        else{utility.twoScreens = false;

        }

        PopularMoviesSyncAdapter.syncImmediately(getApplicationContext());
        //PopularMoviesSyncAdapter.setPeriodicSyncUp(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

           int id = item.getItemId();


        //Settings chosen
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);

            startActivity(intent);


                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Uri movieUri) {

        if (utility.twoScreens) {
            Bundle args = new Bundle();
            args.putParcelable("uri", movieUri);
            //Put parcable movie in here and use above to set
            // this will enable data for movies to be saved


            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_placeholder, movieDetailFragment, MOVIE_FRAG_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this,MovieDetailActivity.class)
                                    .setData(movieUri);
                                    startActivity(intent);



        }

    }
    @Override
    public void onFirstLoad(Uri movieUri){

        mFirstLoadUri = movieUri;




    }


}
