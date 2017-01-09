package org.sco.movieratings;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends CursorRecyclerAdapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mDataset;

    private Context mContext;

    public MovieAdapter(Context context, Cursor c) {
        super(context, c);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        Picasso.with(mContext)
                .load(cursor.getString(MainActivityFragment.COL_POSTER_PATH))
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private ImageView poster;

        public ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
        }

        @Override
        public void onClick(View view) {
            Movie movie = mDataset.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(),DetailActivity.class);
            intent.putExtra("movie", movie);
            view.getContext().startActivity(intent);
        }
    }


}
