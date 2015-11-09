package au.com.fintechapps.popularmovies.content;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by Admin on 4/11/2015.
 */
public interface MyMovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement public static final String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @NotNull public static final String MOVIE_DB_ID = "movie_db_id";
    @DataType(DataType.Type.TEXT) @DefaultValue("NotSupplied")   public static final String ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT) @DefaultValue("NotSupplied")  public static final String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT) @DefaultValue("NotSupplied") public static final String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT) @DefaultValue("NotSupplied")  public static final String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.REAL)  public static final String POPULARITY = "popularity";
    @DataType(DataType.Type.TEXT)  public static final String TITLE = "title";
    @DataType(DataType.Type.BLOB)  public static final String VIDEO = "video";
    @DataType(DataType.Type.REAL)  public static final String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.INTEGER) public static final String VOTE_COUNT = "vote_count";
    @DataType(DataType.Type.INTEGER) public static final String USER_FAVOURITE = "user_favourite";
    @DataType(DataType.Type.TEXT) @NotNull public static final String SOURCE_QUERY = "source_query";
    @DataType(DataType.Type.TEXT) @DefaultValue("NotSupplied")  public static final String BACKDROP_PATH = "backdrop_path";
}

