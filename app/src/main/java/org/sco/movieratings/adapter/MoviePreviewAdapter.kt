package org.sco.movieratings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.sco.movieratings.R
import org.sco.movieratings.api.models.Preview

private const val TAG = "MoviePreviewAdapter"

class MoviePreviewAdapter(val previews: ArrayList<Preview>, private val callback: Callback) : RecyclerView.Adapter<MoviePreviewAdapter.MoviePreviewViewHolder>() {
    interface Callback {
        fun view(preview: Preview)
    }
    class MoviePreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var playButton: ImageView = view.findViewById(R.id.playButton)
        var trailer: TextView = view.findViewById(R.id.trailerName)
    }

    fun add(previewsList: List<Preview>) {
        previews.clear()
        previews.addAll(previewsList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return previews.size
    }

    override fun onBindViewHolder(holder: MoviePreviewViewHolder, position: Int) {
        val preview: Preview = previews[position]
        holder.trailer.text =
            holder.itemView.resources.getString(R.string.preview_title, preview.site, preview.name)
        holder.playButton.setOnClickListener {
            callback.view(preview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviePreviewViewHolder {
        return MoviePreviewViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_list_item, parent, false))
    }

}