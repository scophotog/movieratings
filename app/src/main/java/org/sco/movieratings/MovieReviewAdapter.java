package org.sco.movieratings;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sco.movieratings.data.models.Preview;
import org.sco.movieratings.data.models.Review;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieReviewAdapter.class.getSimpleName();

    private ArrayList<Review> mReviews;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView author;
        private TextView content;

        public ViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.review_author);
            content = (TextView) v.findViewById(R.id.review_content);
        }
    }

    public MovieReviewAdapter(ArrayList<Review> reviews) {
        this.mReviews= reviews;
    }

    public void add(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Review review = mReviews.get(position);

        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

}
