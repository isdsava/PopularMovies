package au.com.fintechapps.popularmovies.content;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;


@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private MovieDatabase(){}

    public static final int VERSION = 4;

    public static class Tables {
        @Table(MovieColumns.class)
        public static final String MOVIES = "movies"; }

    public static class Reviews {
        @Table(UserReviewsColumns.class)
        public static final String REVIEWS = "reviews";
    }

    public static class MyMovies {
        @Table(MyMovieColumns.class)
        public static final String MY_MOVIES = "my_movies";
    }

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
        Log.v("popular","creating");
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

}