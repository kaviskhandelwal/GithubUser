package com.github.android.githubusers.core.extensions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.github.android.githubusers.core.networking.Response
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

fun <T> PublishSubject<T>.toLiveData(compositeDisposable: CompositeDisposable): LiveData<T> {
    val data = MutableLiveData<T>()
    compositeDisposable.add(this.subscribe({ t: T -> data.value = t }))
    return data
}

fun <T> PublishSubject<Response<T>>.failed(e: Throwable) {
    with(this){
        loading(false)
        onNext(Response.failure(e))
    }
}

fun <T> PublishSubject<Response<T>>.success(t: T) {
    with(this){
        loading(false)
        onNext(Response.success(t))
    }
}

fun <T> PublishSubject<Response<T>>.loading(isLoading: Boolean) {
    this.onNext(Response.loading(isLoading))
}
