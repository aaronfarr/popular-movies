package com.aaronfarr.popularmovies.Movies;

import android.support.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Movies {

    private ArrayList<Movie> movies = new ArrayList<>();
    public int MOVIES_FILTER_POPULAR = 1;
    public int MOVIES_FILTER_TOP_RATED = 2;

    public int parseJSON(JSONObject json) throws JSONException {
        String JSON_RESULTS_NODE = "results";
        JSONArray results = json.getJSONArray(JSON_RESULTS_NODE);
        for( int i=0; i<results.length(); i++ ) {
            JSONObject jsonMovie = results.getJSONObject(i);
            Movie movie = new Movie(jsonMovie);
            this.add(movie);
        }
        return 0;
    }

    public boolean add( Movie movie ) {
        int previousCount = movies.size();
        movies.add(movie);
        return movies.size() == ++previousCount;
    }

    @Nullable
    public Movie get(int position) {
        if( position >= 0 && position < movies.size() ) {
            return movies.get(position);
        }
        return null;
    }

    public void filter( int tag ) {
        if( tag == MOVIES_FILTER_TOP_RATED ) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return (int) (movie2.getRating() * 10) - (int) (movie1.getRating() * 10);
                }
            });
        }
        if( tag == MOVIES_FILTER_POPULAR ) {
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie movie1, Movie movie2) {
                    return movie2.getVoteCount() - movie1.getVoteCount();
                }
            });
        }
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}