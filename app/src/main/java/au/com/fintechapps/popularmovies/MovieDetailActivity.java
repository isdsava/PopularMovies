package au.com.fintechapps.popularmovies;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


/**
 * Created by Admin on 15/09/2015.
 *
 * Provides a summary and snapshot of ratings plus release date.
 * Is sent a parcelable object from the Movie Fragment contain all movie info
 */
public class MovieDetailActivity extends ActionBarActivity {

      public MovieDetailActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        //Start the movie detail fragment
        if(savedInstanceState==null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.container, new MovieDetailFragment())
                    .commit();


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Movies detail fragment
    public static class MovieDetailFragment extends  Fragment{

        final String IMG_URL = "http://image.tmdb.org/t/p/w185//";


        public MovieDetailFragment(){}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load up menu
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
        }

       @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){

            View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

            Intent intent = getActivity().getIntent();
            if(intent !=null){
                try {
                    //Grab the parcelable object movie and mine it's brain for nourishment
                    Movie movie = intent.getExtras().getParcelable("aRay");
                    ((TextView) rootView.findViewById(R.id.movie_name_detail)).setText(movie.movieName);
                    ImageView imgPoster = (ImageView) rootView.findViewById(R.id.movie_poster_detail);
                    ((TextView) rootView.findViewById(R.id.movie_date_detail)).setText(movie.releaseDate);
                    ((TextView) rootView.findViewById(R.id.movie_duration_detail)).setText(movie.userRating );
                    ((TextView) rootView.findViewById(R.id.movie_votes_detail)).setText(movie.voteCount);
                    ((TextView) rootView.findViewById(R.id.movie_plot_detail)).setText(movie.movieSynopis);

                    String imgUrl = IMG_URL + movie.posterPath;

                    //Picasso handles caching etc and will know if it has this locally or need to call
                       Picasso.with(getActivity())
                            .load(imgUrl)
                            .placeholder(R.drawable.place_holder)
                            .error(R.drawable.error)
                            .fit()
                            .centerInside()
                            .into(imgPoster);

                }
                catch ( Exception e) {

                }

            }

            return rootView;

        }
    }
}
