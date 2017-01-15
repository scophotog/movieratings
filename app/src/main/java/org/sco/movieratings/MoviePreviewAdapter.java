package org.sco.movieratings;

import java.util.ArrayList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sco.movieratings.data.models.Preview;

public class MoviePreviewAdapter extends RecyclerView.Adapter<MoviePreviewAdapter.ViewHolder> {
    private static final String LOG_TAG = MoviePreviewAdapter.class.getSimpleName();

    private ArrayList<Preview> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView playButton;
        private TextView trailer;

        public ViewHolder(View v) {
            super(v);
            playButton = (ImageView) v.findViewById(R.id.playButton);
            trailer = (TextView) v.findViewById(R.id.trailerName);
        }
    }

    public MoviePreviewAdapter(ArrayList<Preview> myDataset) {
        this.mDataset = myDataset;
        notifyDataSetChanged();
    }

    public void add(Preview item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void clear() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Preview previewResult = mDataset.get(position);

        holder.trailer.setText(previewResult.getName());

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Play Preview", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
