package org.sco.movieratings.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.sco.movieratings.CursorRecyclerViewAdapter;
import org.sco.movieratings.MovieActivity;
import org.sco.movieratings.MovieFragment;
import org.sco.movieratings.R;
import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.models.Movie;

import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    private Context mContext;
    List<Movie> mMovies;

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        if (movies == null) {
            this.mMovies = new ArrayList<>();
        } else {
            mMovies = movies;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView poster;

        public ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
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
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieActivity.class)
                        .putExtra(MovieFragment.MOVIE, mMovies.get(position));
                mContext.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}
