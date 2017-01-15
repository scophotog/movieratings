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

public class MovieListAdapter extends CursorRecyclerViewAdapter<MovieListAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    private Context mContext;
    private Cursor mCursor;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private ImageView poster;

        public ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
            mView = v;
        }

    }

    public MovieListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        final ViewHolder holder = viewHolder;
        mCursor = cursor;
        String posterPath = mCursor.getString(MainActivityFragment.COL_POSTER_PATH);
        Picasso.with(mContext)
                .load(posterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);

        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                mCursor.moveToPosition(position);
                String movieId = mCursor.getString(MainActivityFragment.COL_MOVIE_API_ID);
                Intent intent = new Intent(mContext, MovieActivity.class)
                        .setData(MovieContract.MovieEntry.buildMovieId(movieId));
                mContext.startActivity(intent);
            }

        });
    }

}
