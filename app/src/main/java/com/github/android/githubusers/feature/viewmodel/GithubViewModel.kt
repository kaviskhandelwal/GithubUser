package com.github.android.githubusers.feature.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.github.android.githubusers.core.extensions.performOnBack
import com.github.android.githubusers.core.extensions.toLiveData
import com.github.android.githubusers.core.networking.Response
import com.github.android.githubusers.feature.model.GithubDataContract
import com.github.android.githubusers.feature.model.GithubResponse
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Predicate
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class GithubViewModel(private val repo: GithubDataContract.Repository,
                      private val compositeDisposable: CompositeDisposable) : ViewModel() {


    private var publishSubject : PublishSubject<String> ?= null
    val mGetUserLiveData : LiveData<Response<GithubResponse>> by lazy {
        repo.getUserResponse.toLiveData(compositeDisposable)
    }

    fun searchUser(searchTerm: String) {
        if (publishSubject == null) {
            publishSubject = PublishSubject.create<String>()
            publishSubject?.debounce(300, TimeUnit.MILLISECONDS)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.filter(Predicate { it: String ->
                        return@Predicate it.isNotEmpty()
                    })
                    ?.distinctUntilChanged()
                    ?.subscribe({
                        repo.getUser("$it+repos:>42+followers:>1000")
                    })
        }
        publishSubject?.onNext(Uri.encode(searchTerm))
    }
}