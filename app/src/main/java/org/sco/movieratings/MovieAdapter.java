package org.sco.movieratings;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by sargenzi on 12/2/16.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

    public MovieAdapter(Activity c, List<Movie> movie) {
        super(c, 0, movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        Movie movie = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.moviePoster);
        imageView.setImageResource(R.drawable.sample_0);

        Picasso.with(getContext())
                .load(movie.poster_path)
                .placeholder(R.drawable.sample_0)
                .error(R.drawable.sample_0)
                .noFade()
                .into(imageView);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(movie.title + " \n" + movie.vote_average + " " + movie.popularity);

        return convertView;
    }
}
