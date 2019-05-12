package com.moustafasamhoury.githubchallenge.features.reposlist

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.moustafasamhoury.githubchallenge.R
import com.moustafasamhoury.githubchallenge.base.ui.GithubReposActivity
import com.moustafasamhoury.githubchallenge.repository.network.StateMonitor
import kotlinx.android.synthetic.main.activity_repos_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

class ReposListActivity : GithubReposActivity() {
    override val layout: Int = R.layout.activity_repos_list

    private val reposListViewModel: ReposListViewModel by viewModel()

    private val reposListAdapter: ReposListAdapter by lazy {
        ReposListAdapter()
    }


    override fun onStart() {
        super.onStart()
        reposListViewModel.loadRepositories()
        reposListViewModel.stateLiveData.observe(this, Observer {
            when (val result = it.githubRepositories) {
                StateMonitor.Loading -> {
                    progressBarLoadingReposList.show()
                }
                StateMonitor.Init -> {
                    progressBarLoadingReposList.hide()
                }
                is StateMonitor.Loaded -> {
                    progressBarLoadingReposList.hide()
                    reposListAdapter.submitList(result.result)
                }
                is StateMonitor.Failed -> {
                    progressBarLoadingReposList.hide()
                    showErrorMessageDialog(result.failed)
                }
            }
        })
    }

    private fun showErrorMessageDialog(failed: Throwable) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(failed.message)
            .setCancelable(false)
            .setPositiveButton("Retry") { _, _ ->
                reposListViewModel.loadRepositories()
            }
            .show()
    }

    override fun setupViews() {
        recyclerViewReposList.layoutManager = LinearLayoutManager(this)
        recyclerViewReposList.adapter = reposListAdapter
    }

    override fun cleanupResources() {

    }

}