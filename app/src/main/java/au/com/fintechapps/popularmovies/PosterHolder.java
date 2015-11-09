package au.com.fintechapps.popularmovies;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Admin on 9/11/2015.
 */
public class PosterHolder {
     public final ImageView imgCast;

    public PosterHolder(View view){
        imgCast = (ImageView) view.findViewById(R.id.movie_poster);

    }
}