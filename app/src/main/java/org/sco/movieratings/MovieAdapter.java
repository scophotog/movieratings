package org.sco.movieratings;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.sco.movieratings.data.MovieContract;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends CursorRecyclerViewAdapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private Cursor mCursor;

    public MovieAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

        holder.cursor = cursor;
        String posterPath = cursor.getString(MainActivityFragment.COL_POSTER_PATH);
        Picasso.with(mContext)
                .load(posterPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Cursor cursor = getCursor();
//
//                if (cursor != null) {
//                    ((MainActivityFragment.Callback) mContext)
//                            .onItemSelected(MovieContract.MovieEntry.buildMovieId(mMovieId));
//                }
////                Context context = view.getContext();
////                Intent intent = new Intent(context, DetailActivity.class)
////                        .setData(MovieContract.MovieEntry.buildMovieId(mMovieId));
////                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view,mContext,mCursor);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView poster;
        private Context context;
        private Cursor cursor;

        public ViewHolder(View itemView, Context context, Cursor cursor) {
            super(itemView);
            this.context = context;
            this.cursor = cursor;
            itemView.setOnClickListener(this);
            poster = (ImageView) itemView.findViewById(R.id.moviePoster);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            cursor.moveToPosition(position);
            String movieId = cursor.getString(MainActivityFragment.COL_MOVIE_API_ID);
//            if (cursor != null) {
//                ((MainActivityFragment.Callback) mContext)
//                            .onItemSelected(MovieContract.MovieEntry.buildMovieId(movieId));
//                }

            Intent intent = new Intent(context,DetailActivity.class)
                    .setData(MovieContract.MovieEntry.buildMovieId(movieId));
            Log.d(LOG_TAG,"build movie id " + MovieContract.MovieEntry.buildMovieId(movieId).toString());
            context.startActivity(intent);
        }
    }

}
