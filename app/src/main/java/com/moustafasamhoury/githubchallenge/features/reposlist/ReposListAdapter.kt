package com.moustafasamhoury.githubchallenge.features.reposlist

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moustafasamhoury.githubchallenge.R
import com.moustafasamhoury.githubchallenge.models.GithubRepo
import kotlinx.android.synthetic.main.item_repo_list.view.*
import kotlinx.android.synthetic.main.item_repo_with_shimmer.view.*

/**
 * @author moustafasamhoury
 * created on Sunday, 05 May, 2019
 */

class ReposListAdapter : PagedListAdapter<GithubRepo, ReposListViewHolder>(REPOS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposListViewHolder =
        ReposListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repo_with_shimmer, parent, false)
        )


    override fun onBindViewHolder(holder: ReposListViewHolder, position: Int) = holder.bind(getItem(position))


    companion object {
        val REPOS_COMPARATOR = object : DiffUtil.ItemCallback<GithubRepo>() {
            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean =
                oldItem.name == newItem.name

            override fun getChangePayload(oldItem: GithubRepo, newItem: GithubRepo): Any = Any()
        }
    }
}


class ReposListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(githubRepo: GithubRepo?) {
        if (githubRepo != null) {
            itemView.rootFrameLayout.visibility = View.VISIBLE
            itemView.shimmerLayout.visibility = View.GONE


            itemView.rootFrameLayout.textViewName.text = githubRepo.name


            //Description may be empty
            if (TextUtils.isEmpty(githubRepo.description)) {
                itemView.rootFrameLayout.textViewDescription.visibility = View.GONE
            } else {
                itemView.rootFrameLayout.textViewDescription.visibility = View.VISIBLE
                itemView.rootFrameLayout.textViewDescription.text = githubRepo.description
            }

            itemView.rootFrameLayout.textViewStarsCount.text = githubRepo.starsCount
        } else {
            itemView.rootFrameLayout.visibility = View.GONE
            itemView.shimmerLayout.visibility = View.VISIBLE
        }
    }

}