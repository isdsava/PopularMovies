package au.com.fintechapps.popularmovies.data;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ReviewObject {

    public long id;
    public long page;
    public List<Review> results = new ArrayList<Review>();
    public long totalPages;
    public long totalResults;
}