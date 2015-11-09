package au.com.fintechapps.popularmovies;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import au.com.fintechapps.popularmovies.content.ColumnsForecastHelper;

/**
 * Created by Admin on 29/10/2015.
 */
public class MovieCursorAdapter extends CursorAdapter{

    private PosterHolder posterHolder;

    public MovieCursorAdapter (Context context,Cursor cursor,int flags){
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //Later use a view holder here for speed etc
        View view = LayoutInflater.from(context).inflate(R.layout.row_movies,parent,false);
        PosterHolder posterHolder = new PosterHolder(view);
        view.setTag(posterHolder);
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Create view holder here etc
        final String IMG_URL = "http://image.tmdb.org/t/p/w185//";
        String imgUrl;
        PosterHolder posterHolder = (PosterHolder) view.getTag();


        imgUrl= IMG_URL + cursor.getString(4);

        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error)
                .fit()
                .into(posterHolder.imgCast);



    }


}
