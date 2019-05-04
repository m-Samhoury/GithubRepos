package com.moustafasamhoury.githubchallenge.repository

import com.moustafasamhoury.githubchallenge.repository.network.RetrofitGithubService

/**
 * This repository is our source of truth, we fetch all the data using this repository
 * It may fetch the required data from the network layer or from the local db layer
 *
 * @author moustafasamhoury
 * created on Wednesday, 01 May, 2019
 */
class Repository(val service: RetrofitGithubService) {

}
 