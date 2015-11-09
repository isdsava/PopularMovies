package au.com.fintechapps.popularmovies.data;

/**
 * Created by Admin on 26/10/2015.
 */
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


    public interface IApiMethods{

       @GET("/3/discover/movie")
       Call<MovieObject> getMovies(
                @Query("api_key") String key,
                @Query("sort_by") String sort

       );

        @GET("/3/movie/{ids}/videos")
            Call<TrailerObject> getTrailers(
                @Path("ids") String ids,
                @Query("api_key") String key
        );

        @GET("/3/movie/{ids}/reviews")
        Call<ReviewObject> getReviews(
                @Path("ids") String ids,
                @Query("api_key") String key
        );
    }


