package com.github.android.githubusers.feature.model

import com.github.android.githubusers.core.networking.Response
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface GithubDataContract {
    interface Repository {
        val getUserResponse:PublishSubject<Response<GithubResponse>>
        fun getUser(search: String):PublishSubject<Response<GithubResponse>>
    }

    interface Local {
       // left unimplemented intentionally
    }

    interface Remote {
        fun getUser(search: String):Single<GithubResponse>
    }
}