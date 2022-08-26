package org.sco.movieratings.moviedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.sco.movieratings.R
import org.sco.movieratings.api.response.MoviePreview

class MoviePreviewAdapter :
    RecyclerView.Adapter<MoviePreviewAdapter.MoviePreviewViewHolder>() {

    var moviePreviews: List<MoviePreview> = emptyList()
    var previewClickListener: (MoviePreview) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePreviewViewHolder {
        return MoviePreviewViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return moviePreviews.size
    }

    override fun onBindViewHolder(holder: MoviePreviewViewHolder, position: Int) {
        if (moviePreviews.isNotEmpty()) {
            val moviePreview: MoviePreview = moviePreviews[position]
            holder.trailer.text =
                holder.itemView.resources.getString(R.string.preview_title, moviePreview.site, moviePreview.name)
            holder.playButton.setOnClickListener {
                previewClickListener(moviePreview)
            }
        }
    }

    class MoviePreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var playButton: ImageView = view.findViewById(R.id.playButton)
        var trailer: TextView = view.findViewById(R.id.trailerName)
    }
}