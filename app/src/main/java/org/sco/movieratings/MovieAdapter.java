package org.sco.movieratings;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.sco.movieratings.data.MovieContract;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends CursorRecyclerAdapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;

    private String mPosterPath;
    private String mMovieId;

    public MovieAdapter(Context context, Cursor c) {
        super(context, c);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor c) {
        this.mPosterPath = c.getString(MainActivityFragment.COL_POSTER_PATH);
        this.mMovieId = c.getString(MainActivityFragment.COL_MOVIE_API_ID);
        Picasso.with(mContext)
                .load(mPosterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailActivity.class)
                        .setData(MovieContract.MovieEntry.buildMovieId(mMovieId));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;

        public ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
        }

    }

}
