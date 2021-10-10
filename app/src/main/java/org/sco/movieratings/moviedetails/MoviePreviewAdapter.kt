package org.sco.movieratings.moviedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.sco.movieratings.R
import org.sco.movieratings.api.response.Preview

class MoviePreviewAdapter :
    RecyclerView.Adapter<MoviePreviewAdapter.MoviePreviewViewHolder>() {

    var previews: List<Preview> = emptyList()
    var previewClickListener: (Preview) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePreviewViewHolder {
        return MoviePreviewViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return previews.size
    }

    override fun onBindViewHolder(holder: MoviePreviewViewHolder, position: Int) {
        if (previews.isNotEmpty()) {
            val preview: Preview = previews[position]
            holder.trailer.text =
                holder.itemView.resources.getString(R.string.preview_title, preview.site, preview.name)
            holder.playButton.setOnClickListener {
                previewClickListener(preview)
            }
        }
    }

    class MoviePreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var playButton: ImageView = view.findViewById(R.id.playButton)
        var trailer: TextView = view.findViewById(R.id.trailerName)
    }
}