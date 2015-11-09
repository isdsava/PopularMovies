package au.com.fintechapps.popularmovies;

/**
 * Created by Admin on 16/09/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;


/*
Simple object to hold the movie data and parcelable so it can be used with intents
 */
public class Movie implements Parcelable{

    public int movieId;
    public int movieDbId;
    public String originalTitle;
    public String overView;
    public String releaseDate;
    public String posterPath;
    public double popularity;
    public String title;
    public double voteAverage;
    public int voteCount;


    public Movie(int movieId,int movieDbId,String originalTitle,String overView,String releaseDate,String posterPath,double popularity,String title,double voteAverage, int voteCount) {

        this.movieId = movieId;
        this.movieDbId = movieDbId;
        this.originalTitle =originalTitle;
        this.overView =overView;
        this.releaseDate =releaseDate;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Movie() {

    }

    private Movie(Parcel in){
        movieId = in.readInt();
        movieDbId = in.readInt();
        originalTitle = in.readString();
        overView = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        popularity = in.readDouble();
        title = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public String toString() {return posterPath + "--" +title;}

    @Override
    public void writeToParcel(Parcel parcel,int i){
        parcel.writeInt(movieId);
        parcel.writeInt(movieDbId);
        parcel.writeString(originalTitle);
        parcel.writeString(overView);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeDouble(popularity);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(voteCount);
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