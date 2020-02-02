package org.sco.movieratings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.sco.movieratings.R
import org.sco.movieratings.api.models.Review

private const val LOG = "MovieReviewAdapter"

class MovieReviewAdapter(val reviews: ArrayList<Review>) : RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>() {

    class MovieReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var author: TextView = view.findViewById(R.id.review_author)
        var content: TextView = view.findViewById(R.id.review_content)
    }

    fun add(reviewList: List<Review>) {
        reviews.clear()
        reviews.addAll(reviewList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder {
        return MovieReviewViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) {
        val review: Review = reviews[position]
        holder.author.text = review.author
        holder.content.text = review.content
    }
}