package au.com.fintechapps.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.ShareActionProvider;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.fintechapps.popularmovies.content.ColumnsForecastHelper;
import au.com.fintechapps.popularmovies.content.MovieColumns;
import au.com.fintechapps.popularmovies.content.MovieProvider;
import au.com.fintechapps.popularmovies.content.MyMovieColumns;
import au.com.fintechapps.popularmovies.data.IApiMethods;
import au.com.fintechapps.popularmovies.data.ReviewObject;
import au.com.fintechapps.popularmovies.data.Trailer;
import au.com.fintechapps.popularmovies.data.TrailerObject;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Admin on 2/11/2015.
 */
//Movies detail fragment


public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    final String IMG_URL = "http://image.tmdb.org/t/p/w185//";
    final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    public Uri movieUri;
    Uri uri;
    private static final int DETAIL_LOADER = 5;
    private String mSelectionClause;
    private String[] mSelection;
    private int mIdMovie;
    private Movie movie = new Movie();
    private ContentValues mMovieValues = new ContentValues();
    private ArrayList<ReviewObject> mlists = new ArrayList<ReviewObject>();
    private ArrayList<TrailerObject> mTLists = new ArrayList<TrailerObject>();
    private static final String YOUTUBE_SHARE_HASHTAGE= " #PopularMovies";
    private String mYouTubeUrl;
    private ShareActionProvider mShareActionProvider;
    private ProgressBar mBar;

    String[] MOVIE_FORECAST_COLUMNS = {
            MovieColumns._ID,
            MovieColumns.MOVIE_DB_ID,
            MovieColumns.ORIGINAL_TITLE,
            MovieColumns.OVERVIEW,
            MovieColumns.RELEASE_DATE,
            MovieColumns.POSTER_PATH,
            MovieColumns.POPULARITY,
            MovieColumns.TITLE,
            MovieColumns.VIDEO,
            MovieColumns.VOTE_AVERAGE,
            MovieColumns.VOTE_COUNT,
            MovieColumns.SOURCE_QUERY,
            MovieColumns.BACKDROP_PATH
    };

    static MovieDetailFragment newInstance(Uri movieUri) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("uri", movieUri);
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;
    }

    public MovieDetailFragment() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Utility utility = new Utility();

            mBar.setVisibility(View.VISIBLE);

        String queryType = utility.queryType(getActivity());


        if (movieUri != null) {
            return new CursorLoader(getActivity(),
                    movieUri,
                    MOVIE_FORECAST_COLUMNS,
                    null,
                    null,
                    null);
        } else {

            if (queryType.equals("favourites")) {

                mSelectionClause = MyMovieColumns._ID + " > ? AND " + MyMovieColumns.SOURCE_QUERY + " = ?";
                String selection[] = {"0", queryType};
                movieUri = MovieProvider.MyMovies.CONTENT_URI;

                return new CursorLoader(getActivity(),
                        movieUri,
                        MOVIE_FORECAST_COLUMNS,
                        mSelectionClause,
                        selection,
                        null);

            } else {
                mSelectionClause = MovieColumns._ID + " > ? AND " + MovieColumns.SOURCE_QUERY + " = ?";
                String selection[] = {"0", queryType};
                movieUri = MovieProvider.Movies.CONTENT_URI;

                CursorLoader curchaps =    new CursorLoader(getActivity(),
                        movieUri,
                        MOVIE_FORECAST_COLUMNS,
                        mSelectionClause,
                        selection,
                        null);

                Log.v("popular","currchaps");

                return curchaps;


            }


        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (!data.moveToFirst()) {
            return;
        } else {


            mMovieValues.put(MyMovieColumns.MOVIE_DB_ID, data.getInt(ColumnsForecastHelper.COL_MOVIE_DB_ID));
            mMovieValues.put(MyMovieColumns.ORIGINAL_TITLE, data.getString(ColumnsForecastHelper.COL_ORIG_TITLE));
            mMovieValues.put(MyMovieColumns.OVERVIEW, data.getString(ColumnsForecastHelper.COL_OVERVIEW));
            mMovieValues.put(MovieColumns.RELEASE_DATE, data.getString(ColumnsForecastHelper.COL_RELEASE_DATE));
            mMovieValues.put(MyMovieColumns.POSTER_PATH, data.getString(ColumnsForecastHelper.COL_POSTER_PATH));
            mMovieValues.put(MyMovieColumns.POPULARITY, data.getDouble(ColumnsForecastHelper.COL_POPULARITY));
            mMovieValues.put(MyMovieColumns.TITLE, data.getString(ColumnsForecastHelper.COL_TITLE));
            mMovieValues.put(MyMovieColumns.VOTE_AVERAGE, data.getDouble(ColumnsForecastHelper.COL_VOTE_AVERAGE));
            mMovieValues.put(MyMovieColumns.VOTE_COUNT, data.getInt(ColumnsForecastHelper.COL_VOTE_COUNT));
            mMovieValues.put(MyMovieColumns.SOURCE_QUERY, "favourites");//Relevant if saved
            mMovieValues.put(MyMovieColumns.BACKDROP_PATH, data.getInt(ColumnsForecastHelper.COL_BACKDROP_PATH));

            TextView tvName = (TextView) getView().findViewById(R.id.movie_name_detail);
            tvName.setText(data.getString(ColumnsForecastHelper.COL_ORIG_TITLE));

            TextView tvDetail = (TextView) getView().findViewById(R.id.movie_date_detail);
            tvDetail.setText("Released: "+ data.getString(ColumnsForecastHelper.COL_RELEASE_DATE).substring(0,4));

            TextView tvPop = (TextView) getView().findViewById(R.id.movie_votes_detail);
            tvPop.setText("Voting: " + Double.toString(data.getDouble(ColumnsForecastHelper.COL_VOTE_AVERAGE)).substring(0,1) + "/10");

            TextView tvPlot = (TextView) getView().findViewById((R.id.movie_plot_detail));
            tvPlot.setText(data.getString(ColumnsForecastHelper.COL_OVERVIEW));

            mIdMovie = data.getInt(ColumnsForecastHelper.COL_MOVIE_DB_ID);

            String imgUrl = IMG_URL + data.getString(ColumnsForecastHelper.COL_POSTER_PATH);
            ImageView imgPoster = (ImageView) getView().findViewById(R.id.movie_poster_detail);

            try {  //Picasso handles caching etc and will know if it has this locally or need to call
                Picasso.with(getActivity())
                        .load(imgUrl)
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.error)
                         .into(imgPoster);

            } catch (Exception e) {

            }

            mBar.setVisibility(View.VISIBLE);

            GetReviewsTask getReviewsTask = new GetReviewsTask();
                getReviewsTask.execute(Integer.toString(data.getInt(ColumnsForecastHelper.COL_MOVIE_DB_ID)));

           GetTrailerTask getTrailerTask = new GetTrailerTask();
                getTrailerTask.execute(Integer.toString(data.getInt(ColumnsForecastHelper.COL_MOVIE_DB_ID)));


        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        uri = getArguments().getParcelable("uri");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load up menu
        setHasOptionsMenu(true);




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_movie_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

    }

    private Intent shareYouTubeUrl() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mYouTubeUrl + YOUTUBE_SHARE_HASHTAGE);
        return shareIntent;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle arg = getArguments();
        if (arg != null) {

            movieUri = arg.getParcelable("uri");
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        Button butFave = (Button) rootView.findViewById(R.id.button_fav);
        butFave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uuuRiii = getActivity().getContentResolver().insert(MovieProvider.MyMovies.CONTENT_URI, mMovieValues);
                Toast.makeText(getActivity(), "This movie is now in your favourites", Toast.LENGTH_LONG).show();

            }
        });


        return rootView;

    }


    public class GetTrailerTask extends AsyncTask<String, Void, Response<TrailerObject>> {

        @Override
        protected Response<TrailerObject> doInBackground(String... params) {

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "6a51b1b7c06c245da2160c831051948e";
            Log.v("popular", "firedOnPerf");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            IApiMethods iApiMethods = retrofit.create(IApiMethods.class);

            Call<TrailerObject> call = iApiMethods.getTrailers(params[0], API_KEY);

            try {
                Response<TrailerObject> response = call.execute();
                return response;
            } catch (IOException e) {
                return null;

            }

        }

        @Override
        protected  void onPreExecute(){
            mBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Response<TrailerObject> result) {
            if (result != null) {

                View liner = (LinearLayout) getView().findViewById(R.id.linear_bottom_two);
               final TrailerObject trailerObject = result.body();

                for (int i=0 ; i< trailerObject.results.size();i++) {

                    TextView tv = new TextView(getActivity());
                    tv.setText(trailerObject.results.get(i).name);
                    tv.setTextAppearance(getActivity(), R.style.TextFly);
                    tv.setTextSize(20);
                    tv.setPadding(14, 12, 15, 2);
                     LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(layoutParams);
                    ((LinearLayout) liner).addView(tv);


                    ImageButton button = new ImageButton(getActivity());
                    button.setImageResource(R.drawable.youtube);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            watchYouTubeVideo(trailerObject.results.get(0).key);
                        }
                    });
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    button.setLayoutParams(layoutParams2);
                    ((LinearLayout) liner).addView(button);

                        mYouTubeUrl = YOUTUBE_URL + trailerObject.results.get(0).key;
                        mShareActionProvider.setShareIntent(shareYouTubeUrl());

                }


                mBar.setVisibility(View.INVISIBLE);

            }
        }
    }

    public class GetReviewsTask extends AsyncTask<String, Void, Response<ReviewObject>> {

        @Override
        protected Response<ReviewObject> doInBackground(String... params) {


             final String MOVIE_BASE_URL = "http://api.themoviedb.org/";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String API_KEY = "6a51b1b7c06c245da2160c831051948e";

            Log.v("popular", "firedOnPerf");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            IApiMethods iApiMethods = retrofit.create(IApiMethods.class);

            Call<ReviewObject> call = iApiMethods.getReviews(params[0], API_KEY);
            try {
                Response<ReviewObject> response = call.execute();
                return response;
            }
            catch (IOException e){
                return null;

            }

        }
        @Override
        protected  void onPreExecute(){
            mBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected void onPostExecute(Response<ReviewObject> result) {
            if (result != null) {
                View liner = (LinearLayout) getView().findViewById(R.id.linear_bottom);
                ReviewObject reviewObject = result.body();

                    for (int i=0 ; i< reviewObject.results.size();i++) {

                        TextView tv = new TextView(getActivity());
                        tv.setText(reviewObject.results.get(i).author);
                        tv.setTextAppearance(getActivity(), R.style.TextFly);
                        tv.setTextSize(20);
                        tv.setPadding(14, 12, 15, 2);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        tv.setLayoutParams(layoutParams);
                        ((LinearLayout) liner).addView(tv);

                        TextView tv_2 = new TextView(getActivity());
                        tv_2.setText(reviewObject.results.get(i).content);
                        tv_2.setPadding(15, 2, 15, 20);
                         tv_2.setLayoutParams(layoutParams);

                        ((LinearLayout) liner).addView(tv_2);

                    }

                mBar.setVisibility(View.INVISIBLE);



            }

        }
    }




    public void watchYouTubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse(YOUTUBE_URL+id));
            startActivity(intent);
        }
    }
}