package au.com.fintechapps.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import au.com.fintechapps.popularmovies.content.MovieColumns;
import au.com.fintechapps.popularmovies.content.MovieProvider;
import au.com.fintechapps.popularmovies.sync.PopularMoviesSyncAdapter;

/**
 * Created by Admin on 26/10/2015.
 */
public class TestGeneral extends AndroidTestCase {


    public void testMeThingy(){
       //PopularMoviesSyncAdapter.syncImmediately(getContext());

       String mSelectionClause = MovieColumns.MOVIE_DB_ID  + " IN ( ? )";
        String selection[] = {" Select my_movies.movies_db_id from my_movies "};
        Uri mMovieUri = MovieProvider.Movies.CONTENT_URI;

        String[] MOVIE_FORECAST_TEST= {
                MovieColumns._ID,
                MovieColumns.MOVIE_DB_ID};
        //CursorLoader cursorLoader = new CursorLoader(getActivity(),
        //      mMovieUri,
        //    MOVIE_FORECAST_COLUMNS,
        //  mSelectionClause,
        //selection,
        //null);

        Cursor cursor = getContext().
                getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                MOVIE_FORECAST_TEST,
                mSelectionClause,
                selection,
                null);

        if(cursor.moveToFirst()){
            Log.v("popular", "ID" + Integer.toString(cursor.getInt(1)));}
        //while (cursor1.moveToNext()) {
        //  Log.v("popular", "ID" + Integer.toString(cursor1.getInt(1)));
        //}
    }
}
