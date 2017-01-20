package org.sco.movieratings.api.response;

import java.io.Serializable;
import java.util.List;

import org.sco.movieratings.api.models.Movie;

import com.google.gson.annotations.SerializedName;

public class MoviesResponse implements Serializable {

    private int page;

    @SerializedName("results")
    private List<Movie> movies;

    @SerializedName("total_results")
    private int totalMovieCount;

    @SerializedName("total_pages")
    private int totalPageCount;

    public List<Movie> getMovies() {
        return movies;
    }

}