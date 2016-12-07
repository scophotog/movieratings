package org.sco.movieratings;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mDataset;

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

    public MovieAdapter(ArrayList<Movie> myDataset) {
        this.mDataset = myDataset;
        notifyDataSetChanged();
    }

    public void add(Movie item) {
        mDataset.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_movie, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = mDataset.get(position);
        Context context = holder.poster.getContext();
        Picasso.with(context)
                .load(movie.poster_path)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),DetailActivity.class);
                intent.putExtra("movie", movie);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
