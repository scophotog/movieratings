package org.sco.movieratings.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.BuildConfig;
import org.sco.movieratings.R;
import org.sco.movieratings.api.models.Movie;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.subjects.PublishSubject;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieListAdapter.class.getSimpleName();

    private final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";

    private final List<Movie> mMovies;
    private final PublishSubject<Movie> clickStream;

    public MovieListAdapter(@NonNull PublishSubject<Movie> clickStream) {
        this(Collections.<Movie>emptyList(), clickStream);
    }

    public MovieListAdapter(@NonNull List<Movie> movies, @NonNull PublishSubject<Movie> clickStream) {
        this.clickStream = clickStream;
        this.mMovies = new ArrayList<>(movies);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @VisibleForTesting
        protected static final String ROW_TEXT = "ROW_TEXT";

        @VisibleForTesting
        protected static final String ITEM_TEXT_FORMAT = "item: %d";

        @VisibleForTesting
        protected String makeItem(int forRow) {
            return String.format(ITEM_TEXT_FORMAT, forRow);
        }

        public ImageView poster;
        public Movie mMovie;
        public TextView rowId;

        public ViewHolder(View view) {
            super(view);
            poster = view.findViewById(R.id.moviePoster);
            rowId = view.findViewById(R.id.debugText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickStream.onNext(mMovie);
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
        holder.mMovie = mMovies.get(position);
        Picasso.get()
                .load(IMAGE_PATH + mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.image_not_found)
                .into(holder.poster);

        if (!BuildConfig.DEBUG) {
            holder.rowId.setText(holder.makeItem(position));
            holder.rowId.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public List<Movie> getItems() {
        return mMovies;
    }

}
