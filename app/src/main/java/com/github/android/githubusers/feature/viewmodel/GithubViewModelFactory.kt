package com.github.android.githubusers.feature.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.android.githubusers.feature.model.GithubDataContract
import io.reactivex.disposables.CompositeDisposable

class GithubViewModelFactory(private val repository: GithubDataContract.Repository, private val compositeDisposable: CompositeDisposable) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GithubViewModel(repository, compositeDisposable) as T
    }
}