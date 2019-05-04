package com.moustafasamhoury.githubchallenge.features.reposlist

import android.os.Bundle
import com.moustafasamhoury.githubchallenge.R
import com.moustafasamhoury.githubchallenge.base.ui.GithubReposActivity
import kotlinx.android.synthetic.main.activity_repos_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

class ReposListActivity : GithubReposActivity() {
    override val layout: Int = R.layout.activity_repos_list

    private val reposListViewModel: ReposListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        reposListViewModel.loadRepositories()
    }

    override fun setupViews() {

    }

    override fun cleanupResources() {

    }

}