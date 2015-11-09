package au.com.fintechapps.popularmovies;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import java.util.ArrayList;

import au.com.fintechapps.popularmovies.content.ColumnsForecastHelper;
import au.com.fintechapps.popularmovies.content.MovieColumns;
import au.com.fintechapps.popularmovies.content.MovieProvider;
import au.com.fintechapps.popularmovies.content.MyMovieColumns;
import au.com.fintechapps.popularmovies.content.UserReviewsColumns;
import au.com.fintechapps.popularmovies.data.MovieObject;
import au.com.fintechapps.popularmovies.data.Result;


import android.support.v4.content.CursorLoader;
import android.widget.Toast;

/**
 * Provides a gridview of the movie data extracted based upon either the default - most popular
 * or the users choice
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<Movie> arrayOfMovies = new ArrayList<Movie>();
    private static final int MOVIE_LOADER = 0;
    private MovieObject responseOfMovies;
    public MovieCursorAdapter movieAdapter;
    private Movie mvh;
    private final String mvKey = "movies";
    private ArrayAdapter<Result> arrayAdapter;


        public MovieFragment() {
    }

    public interface Callback{

        public void onItemSelected(Uri movieUri);
        public void onFirstLoad(Uri movieUri);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        //Settings chosen
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final Utility utility = new Utility();
        Cursor cursor = null;

        movieAdapter = new MovieCursorAdapter(getActivity(),cursor,0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie_posters);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri;
                Cursor cur = (Cursor) parent.getItemAtPosition(position);
                if (cur != null) {

                    String queryType = utility.queryType(getActivity());

                    if (queryType.equals("favourites")) {
                         uri = MovieProvider.MyMovies.withId(cur.getInt(ColumnsForecastHelper.COL_ID));
                    }else {
                         uri = MovieProvider.Movies.withId(cur.getInt(ColumnsForecastHelper.COL_ID));
                    }

                    ((Callback) getActivity()).onItemSelected(uri);

                }

            }
        });


        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        //updateMovies();
    }

    @Override
    public void onResume() {
        super.onResume();

        Utility utility = new Utility();
        boolean refreshNow = utility.refresh(getActivity());
        boolean twoScreens = utility.twoScreens;

        if (refreshNow && twoScreens) {
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);

            Bundle args = new Bundle();
            args.putParcelable("uri", null);

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(args);

            getFragmentManager().beginTransaction().replace(R.id.movie_detail_placeholder, movieDetailFragment, "fragtag")
                    .commit();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.requery_data_key), false);
            edit.apply();

        } else if (refreshNow && !twoScreens) {

              //  Intent intent = new Intent(getActivity(),MovieDetailActivity.class)
              //          .setData(null);
              //  startActivity(intent);
            }

    }

      @Override
    public void onActivityCreated(Bundle savedInstanceState){
          super.onActivityCreated(savedInstanceState);
          getLoaderManager().restartLoader(MOVIE_LOADER, null, this);

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Utility utility = new Utility();

        String queryType = utility.queryType(getActivity());

        if (queryType.equals("favourites")) {
                return queryMyMovies();
        }else {
                return queryMovies(queryType);
        }

    }
    public CursorLoader queryMovies(String queryType){

        Uri moviesUri = MovieProvider.Movies.CONTENT_URI;
        String sortOrder = MovieColumns.VOTE_AVERAGE + " ASC";
        String[] MOVIE_FORECAST_COLUMNS = {
                MovieColumns._ID,
                MovieColumns.ORIGINAL_TITLE,
                MovieColumns.OVERVIEW,
                MovieColumns.POPULARITY,
                MovieColumns.POSTER_PATH,
                MovieColumns.RELEASE_DATE,
                MovieColumns.TITLE,
                MovieColumns.VIDEO,
                MovieColumns.VOTE_AVERAGE,
                MovieColumns.VOTE_COUNT,
                MovieColumns.SOURCE_QUERY
        };



        String selectionClause = MovieColumns.SOURCE_QUERY  + " = ?";
        String selection[] = {queryType};

        CursorLoader cursorLoader =  new CursorLoader(getActivity(),
                moviesUri,
                MOVIE_FORECAST_COLUMNS,
                selectionClause,
                selection,
                null);

return cursorLoader;

    }
    public CursorLoader queryMyMovies(){

        Uri moviesUri = MovieProvider.MyMovies.CONTENT_URI;
        String sortOrder = MyMovieColumns.VOTE_AVERAGE + " ASC";
        String[] MOVIE_FORECAST_COLUMNS = {
                MyMovieColumns._ID,
                MyMovieColumns.ORIGINAL_TITLE,
                MyMovieColumns.OVERVIEW,
                MyMovieColumns.POPULARITY,
                MyMovieColumns.POSTER_PATH,
                MyMovieColumns.RELEASE_DATE,
                MyMovieColumns.TITLE,
                MyMovieColumns.VIDEO,
                MyMovieColumns.VOTE_AVERAGE,
                MyMovieColumns.VOTE_COUNT,
                MyMovieColumns.SOURCE_QUERY

        };



        return new CursorLoader(getActivity(),
                moviesUri,
                MOVIE_FORECAST_COLUMNS,
                null,
                null,
                null);



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Uri uri;

      if( data.moveToFirst()) {
              uri = MovieProvider.Movies.withId(data.getInt(0));
      }
        else {
           uri = MovieProvider.Movies.CONTENT_URI;
      }

         movieAdapter.swapCursor(data);
        ((Callback) getActivity()).onFirstLoad(uri);

      }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }


}







