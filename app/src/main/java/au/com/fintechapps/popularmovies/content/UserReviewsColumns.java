package au.com.fintechapps.popularmovies.content;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Admin on 30/10/2015.

 extends MovieColumns
 */
public interface UserReviewsColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement public static final String _ID = "_id";
    @DataType(DataType.Type.INTEGER) @References(table= MovieDatabase.Tables.MOVIES,column =MovieColumns._ID) String LIST_ID ="listId";
    @DataType(DataType.Type.INTEGER)  public static final String USER_FAVOURITE = "user_favourite";

}
