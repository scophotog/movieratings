package org.sco.movieratings.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.sco.movieratings.R;
import org.sco.movieratings.api.models.Movie;
import org.sco.movieratings.fragment.MovieListFragment;

import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    private final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

    private Context mContext;
    private List<Movie> mMovies;

    private MovieListFragment.Callbacks callbacks;

    public MovieListAdapter(Context context, List<Movie> movies, MovieListFragment.Callbacks callbacks) {
        this.mContext = context;
        this.callbacks = callbacks;

        if (movies == null) {
            this.mMovies = new ArrayList<>();
        } else {
            mMovies = movies;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView poster;
        public Movie mMovie;
        private MovieListFragment.Callbacks callbacks;

        public ViewHolder(View v, MovieListFragment.Callbacks callbacks) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
            this.callbacks = callbacks;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (callbacks != null) {
                callbacks.onItemSelected(mMovie);
            }
        }
    }

    public void add(List<Movie> movies) {
        mMovies.clear();
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);
        ViewHolder vh = new ViewHolder(itemView, callbacks);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mMovie = mMovies.get(position);
        Picasso.with(mContext)
                .load(IMAGE_PATH + mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public List<Movie> getItems() {
        return mMovies;
    }

}
