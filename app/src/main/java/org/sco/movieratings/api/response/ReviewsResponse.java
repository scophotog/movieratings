package org.sco.movieratings.api.response;

import java.io.Serializable;
import java.util.List;

import org.sco.movieratings.api.models.Review;

import com.google.gson.annotations.SerializedName;

public class ReviewsResponse implements Serializable {

    private int id;
    private int page;
    private int totalPages;
    private int totalResults;

    @SerializedName("results")
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

}