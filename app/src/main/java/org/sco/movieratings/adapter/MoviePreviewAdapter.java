package org.sco.movieratings.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.sco.movieratings.R;
import org.sco.movieratings.data.models.Preview;

public class MoviePreviewAdapter extends RecyclerView.Adapter<MoviePreviewAdapter.ViewHolder> {
    private static final String LOG_TAG = MoviePreviewAdapter.class.getSimpleName();

    private ArrayList<Preview> mPreviews;
    private final Callback mCallback;

    public interface Callback {
        void view(Preview preview);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView playButton;
        private TextView trailer;

        public ViewHolder(View v) {
            super(v);
            playButton = (ImageView) v.findViewById(R.id.playButton);
            trailer = (TextView) v.findViewById(R.id.trailerName);
        }
    }

    public MoviePreviewAdapter(ArrayList<Preview> previews, Callback callback) {
        this.mPreviews = previews;
        mCallback = callback;
    }

    public void add(List<Preview> previews) {
        mPreviews.clear();
        mPreviews.addAll(previews);
        notifyDataSetChanged();
    }

    public ArrayList<Preview> getPreviews() {
        return mPreviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.preview_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Preview preview = mPreviews.get(position);

        holder.trailer.setText(preview.getName());

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.view(preview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPreviews.size();
    }

}
