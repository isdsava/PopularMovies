package au.com.fintechapps.popularmovies;

/**
 * Created by Admin on 16/09/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;


/*
Simple object to hold the movie data and parcelable so it can be used with intents
 */
public class Movie implements Parcelable{


    public String movieName;
    public String movieSynopis;
    public String userRating;
    public String releaseDate;
    public String posterPath;
    public String voteCount;

    //Not going to worry about numbers as strings. We are not perfoming any operations on the data
    // so worrying about number validation isnt required.
    public Movie(String movieName,String movieSynopis,String userRating,String releaseDate,String posterPath,String voteCount) {

        this.movieName = movieName;
        this.movieSynopis = movieSynopis;
        this.userRating = userRating;
        this.releaseDate= releaseDate;
        this.posterPath = posterPath;
        this.voteCount = voteCount;
    }

    public Movie() {

    }

    private Movie(Parcel in){
        movieName = in.readString();
        movieSynopis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteCount = in.readString();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public String toString() {return posterPath + "--" + movieName;}

    @Override
    public void writeToParcel(Parcel parcel,int i){
        parcel.writeString(movieName);
        parcel.writeString(movieSynopis);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(voteCount);
    }

    public static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }
        @Override
      public Movie[] newArray(int i){
            return new Movie[i];
        }
    };
}