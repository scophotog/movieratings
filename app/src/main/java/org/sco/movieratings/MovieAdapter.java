package org.sco.movieratings;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

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

        Picasso.with(getContext())
                .load(movie.poster_path)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(imageView);

        return convertView;
    }
}
