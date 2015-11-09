package au.com.fintechapps.popularmovies.content;


import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 26/10/2015.
 */

@ContentProvider(authority = MovieProvider.AUTHORITY,database = MovieDatabase.class)

public final class MovieProvider {

    public static final String AUTHORITY = "au.com.fintechapps.popularmovies.content.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String MOVIES = "movies";
        String REVIEWS = "reviews";
        String MY_MOVIES = "my_movies";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String path : paths) {
            builder.appendPath(path);
        }

        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.Tables.MOVIES)
    public static class Movies {
        @ContentUri(path = Path.MOVIES, type = "vnd.android.cursor.dir/movie")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);


        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)

        public static final Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }

       /** @InexactContentUri(
                name = "MOVIE_DB_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.MOVIE_DB_ID,
                pathSegment = 2)

        public static final Uri withDbId(long id) {
            return buildUri(Path.MOVIES,String.valueOf(id));
        }**/
    }


   @TableEndpoint(table = MovieDatabase.Reviews.REVIEWS)
        public static class Reviews {
        @ContentUri(path = Path.REVIEWS, type = "vnd.android.cursor.dir/review")
        public static final Uri CONTENT_URI = buildUri(Path.REVIEWS);

        @InexactContentUri(
                name = "REVIEW_ID",
                path = Path.REVIEWS + "/#",
                type = "vnd.android.cursor.item/review",
                whereColumn = UserReviewsColumns._ID,
                pathSegment = 1)

        public static  final Uri withId(long id) {
            return buildUri(Path.REVIEWS,String.valueOf(id));
        }
    }

    @TableEndpoint(table = MovieDatabase.MyMovies.MY_MOVIES)
    public static class MyMovies {
        @ContentUri(path = Path.MY_MOVIES,
                    type = "vnd.android.cursor.dir/my_movie")
        public static final Uri CONTENT_URI = buildUri(Path.MY_MOVIES);


        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MY_MOVIES + "/#",
                type = "vnd.android.cursor.item/my_movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)

        public static final Uri withId(long id) {
            return buildUri(Path.MY_MOVIES, String.valueOf(id));
        }

    }

}