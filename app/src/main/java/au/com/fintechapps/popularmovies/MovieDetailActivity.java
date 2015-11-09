package au.com.fintechapps.popularmovies;


import android.graphics.*;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


/**
 * Created by Admin on 15/09/2015.
 *
 * Provides a summary and snapshot of ratings plus release date.
 * Is sent a parcelable object from the Movie Fragment contain all movie info
 */
public class MovieDetailActivity extends ActionBarActivity {

      public MovieDetailActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        //Start the movie detail fragment
        if(savedInstanceState==null){

            Bundle args = new Bundle();
                    args.putParcelable("uri",getIntent().getData());

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                                movieDetailFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.container, movieDetailFragment)
                    .commit();


        }

    }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  Intent intent = new Intent(this, SettingsActivity.class);
            //startActivity(intent);
            //return true;
      //  }

        //return super.onOptionsItemSelected(item);
    //}

}
