package au.com.fintechapps.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Provides a gridview of the movie data extracted based upon either the default - most popular
 * or the users choice
 */
public class MovieFragment extends Fragment {

    private ArrayList<Movie> arrayOfMovies = new ArrayList<Movie>();
    private ImageArrayAdapter movieAdapter;
    private Movie mvh;
    private final String mvKey = "movies";

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean refreshNow = prefs.getBoolean(getString(R.string.requery_data_key), false);

        if (savedInstanceState == null || !savedInstanceState.containsKey(mvKey) || refreshNow) {
            arrayOfMovies = new ArrayList<Movie>();
            updateMovies();
            // Turn off refresh flag
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.requery_data_key), false);
            edit.apply();

        } else {
            //Already have data dont need to requery
            arrayOfMovies = savedInstanceState.getParcelableArrayList(mvKey);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(mvKey, arrayOfMovies);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        movieAdapter = new ImageArrayAdapter(getActivity(),
                R.layout.row_movies,// name of the layout
                R.id.movie_poster, // place holder only we actually have a custom get view
                arrayOfMovies); // Array of movie objects ready to populated.

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie_posters);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                // Get our movie object and have it ready for passing
                //as parcalable object.
                Movie arrayPass = arrayOfMovies.get(position);
                intent.putExtra("aRay", arrayPass);
                startActivity(intent);
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
        // Just making sure we aren't constantly returning to the data source
        // unless something has in our settings then we don't need to requery the data
        // everytime screen rotated etc.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean refreshNow = prefs.getBoolean(getString(R.string.requery_data_key), false);

        if (refreshNow) {
            updateMovies();
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.requery_data_key), false);
            edit.apply();
        }
    }

    private void updateMovies() {
        GetMoviesTask moviesTask = new GetMoviesTask();
        moviesTask.execute("1");
    }

    // Doing our JSON parsing.
    private ArrayList<Movie> getMovieDataFromJson(String moviesJsonStr) throws JSONException {

        final String RESULTS_HDR = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVG = "vote_average";
        final String VOTE_COUNT ="vote_count";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesJsonObj = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJsonObj.getJSONArray(RESULTS_HDR);

        arrayOfMovies.clear();

        for (int i = 0; i < moviesArray.length(); i++) {

            final Movie newMvh = new Movie();
            //Take our JSON strings into our movie object
            JSONObject objMovieName = moviesArray.getJSONObject(i);
            newMvh.movieName = objMovieName.getString(ORIGINAL_TITLE);
            newMvh.posterPath = objMovieName.getString(POSTER_PATH);
            newMvh.movieSynopis = objMovieName.getString(OVERVIEW);
            //just annoys me seeing 10.0 out of 10
            if(objMovieName.getString(VOTE_AVG).equals("10.0")){
                newMvh.userRating ="10/10";
            }
            else {
                newMvh.userRating =objMovieName.getString(VOTE_AVG) + "/10";
            }
            //There was another choice in the API but this works anyway
            newMvh.releaseDate = (objMovieName.getString(RELEASE_DATE)).substring(0, 4);
            newMvh.voteCount =  objMovieName.getString(VOTE_COUNT) + " votes";

            arrayOfMovies.add(i, newMvh);

        }


        return arrayOfMovies;
    }

    /*
        @ArrayList of Movies that is populated with our results after
        JSON string is parsed

     */
    public class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            //No param then no point
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConn = null;
            BufferedReader buffRead = null;
            String moviesJsonStr = null;

            //Grab the users choice or the default for movie views.
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String queryType = sharedPreferences.getString(
                    getString(R.string.sort_key),
                    getString(R.string.sort_default));


            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";
                final String API_KEY = "6a51b1b7c06c245da2160c831051948e";

                Uri uriBuilder = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, queryType)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

                URL url = new URL(uriBuilder.toString());
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setRequestMethod("GET");
                urlConn.connect();

                InputStream inputStream = urlConn.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                buffRead = new BufferedReader(new InputStreamReader(inputStream));
                String movieLine;
                while ((movieLine = buffRead.readLine()) != null) {
                    buffer.append(movieLine);
                }

                if (buffer.length() == 0) {
                    //Dohh no data. Handled it but
                    return null;
                }

                moviesJsonStr = buffer.toString();

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConn != null) {
                    urlConn.disconnect();
                }
                if (buffRead != null) {
                    try {
                        buffRead.close();
                    } catch (final IOException e) {
                        //Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonStr);

            } catch (JSONException e) {

            }


            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                movieAdapter.notifyDataSetChanged();
            }
        }
    }

    /*
        Extending our Array Adapter so we can include images. Using Picasso to handle retrival and caching
     */
    public class ImageArrayAdapter extends ArrayAdapter<Movie> {

        private MovieHolder movieHolder;

        public ImageArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<Movie> objects) {

            super(context, resource, textViewResourceId, objects);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            final String IMG_URL = "http://image.tmdb.org/t/p/w185//";

            mvh = getItem(position);

            Target target;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_movies, parent, false);
                //Holder for speedier access and less traffic.
                movieHolder = new MovieHolder();
                movieHolder.poster = ((ImageView) convertView.findViewById(R.id.movie_poster));
                //Attach it to the view now its created
                convertView.setTag(movieHolder);
            } else {
                //Already exists get it from the view into the holder
                movieHolder = (MovieHolder) convertView.getTag();
            }

            String imgUrl = IMG_URL + mvh.posterPath;

            //Picasso handles caching etc and will know if it has this locally or need to call

            Picasso.with(getContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.error)
                    .fit()
                    .into(movieHolder.poster);


            return convertView;
        }

        //Holder object for posters ImageViews
        private class MovieHolder {
            ImageView poster;

        }

    }


}
