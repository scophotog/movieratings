package org.sco.movieratings;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ArrayList<Movie> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;

        public ViewHolder(View v) {
            super(v);
            poster = (ImageView) v.findViewById(R.id.moviePoster);
        }

    }

    public MovieAdapter(ArrayList<Movie> myDataset) {
        this.mDataset = myDataset;
        notifyDataSetChanged();
    }

/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
            holder = new ViewHolder();
            holder.poster = (ImageView) convertView.findViewById(R.id.moviePoster);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);

        Picasso.with(getContext())
                .load(movie.poster_path)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.poster);

        return convertView;
    }
*/

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
        Movie movie = mDataset.get(position);
        Context context = holder.poster.getContext();
        Picasso.with(context)
                .load(movie.poster_path)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "TOAST", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
