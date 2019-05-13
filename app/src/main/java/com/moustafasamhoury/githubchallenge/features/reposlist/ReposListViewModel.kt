package com.moustafasamhoury.githubchallenge.features.reposlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.moustafasamhoury.githubchallenge.base.GithubReposViewModel
import com.moustafasamhoury.githubchallenge.models.GithubRepo
import com.moustafasamhoury.githubchallenge.repository.Repository
import com.moustafasamhoury.githubchallenge.repository.network.NetworkState
import com.moustafasamhoury.githubchallenge.repository.network.StateMonitor
import com.moustafasamhoury.githubchallenge.utils.DateFormatter
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

data class ReposListState(val githubRepositories: StateMonitor<PagedList<GithubRepo>> = StateMonitor.Init) {

}

class ReposListViewModel(private val repository: Repository) : GithubReposViewModel() {
    private val state: ReposListState = ReposListState()
    val stateLiveData = MutableLiveData<ReposListState>()
    val errorsLiveData = MutableLiveData<NetworkState>()

    init {
        errorsLiveData.observe(this, Observer {
            when (it) {
                NetworkState.Loading -> {
                    stateLiveData.postValue(state)
                }
                NetworkState.Loaded -> {
                    stateLiveData.postValue(state)
                }
                is NetworkState.Error -> {
                    stateLiveData.postValue(
                        state.copy(
                            githubRepositories = StateMonitor.Failed(
                                it.throwable,
                                it.errorAction
                            )
                        )
                    )
                }
            }
        })
    }


    fun loadRepositories() {
        stateLiveData.postValue(state.copy(githubRepositories = StateMonitor.Loading))

        val observable =
            repository.fetchTopGithubRepositoriesPaginated(
                createdAfterDate =
                "created:>${DateFormatter
                    .formatDateToServer(Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_WEEK_IN_MONTH, -30)//last 30 days
                    }.time)}",
                errorsLiveData = errorsLiveData
            )


        observable.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                stateLiveData.postValue(state.copy(githubRepositories = StateMonitor.Loaded(it)))
            }, {
                stateLiveData.postValue(state.copy(githubRepositories = StateMonitor.Failed(it)))
            })
            .disposeOnClear()

    }
}