package org.sco.movieratings.api.response;

import java.io.Serializable;
import java.util.List;

import org.sco.movieratings.api.models.Preview;

import com.google.gson.annotations.SerializedName;

public class PreviewsResponse implements Serializable {

    private int id;

    @SerializedName("results")
    private List<Preview> previews;

    public List<Preview> getPreviews() {
        return previews;
    }

}