package org.sco.movieratings.ui;

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
import org.sco.movieratings.R;
import org.sco.movieratings.data.MovieColumns;
import org.sco.movieratings.data.MovieProvider;

import com.squareup.picasso.Picasso;

public class MovieFavoriteListAdapter extends CursorRecyclerViewAdapter<MovieFavoriteListAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieFavoriteListAdapter.class.getSimpleName();

    private Context mContext;

    public MovieFavoriteListAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView poster;

        public ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final Cursor cursor) {
        DatabaseUtils.dumpCursor(cursor);
        String posterPath = cursor.getString(cursor.getColumnIndex(
                MovieColumns.POSTER_PATH));
        Picasso.with(mContext)
                .load(posterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieActivity.class)
                        .setData(MovieProvider.Movies.withId(cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_ID))));
                mContext.startActivity(intent);
            }

        });
    }

}
