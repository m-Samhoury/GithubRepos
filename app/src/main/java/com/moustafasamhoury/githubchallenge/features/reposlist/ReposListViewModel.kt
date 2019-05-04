package com.moustafasamhoury.githubchallenge.features.reposlist

import com.moustafasamhoury.githubchallenge.base.GithubReposViewModel
import com.moustafasamhoury.githubchallenge.repository.Repository

/**
 * @author moustafasamhoury
 * created on Saturday, 04 May, 2019
 */

class ReposListViewModel(private val repository: Repository) : GithubReposViewModel() {


    fun loadRepositories(page: Int = 1) {

    }
}