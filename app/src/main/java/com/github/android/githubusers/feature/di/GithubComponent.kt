package com.github.android.githubusers.feature.di

import com.github.android.githubusers.common.remote.GithubService
import com.github.android.githubusers.core.di.CoreComponent
import com.github.android.githubusers.core.networking.Scheduler
import com.github.android.githubusers.feature.MainActivity
import com.github.android.githubusers.feature.model.GithubDataContract
import com.github.android.githubusers.feature.model.GithubRemote
import com.github.android.githubusers.feature.model.GithubRepository
import com.github.android.githubusers.feature.viewmodel.GithubViewModelFactory

import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit

@GithubScope
@Component(dependencies = [CoreComponent::class], modules = [GithubModule::class, RepoModule::class])
interface GitHubComponent {
    fun scheduler(): Scheduler
    fun inject(mainActivity: MainActivity)
    fun gitHubService(): GithubService
}

@Module
class GithubModule {

    @Provides
    @GithubScope
    fun githubViewModelFactory(repository: GithubDataContract.Repository,compositeDisposable: CompositeDisposable): GithubViewModelFactory = GithubViewModelFactory(repository,compositeDisposable)

    @Provides
    @GithubScope
    fun listRepo(remote: GithubDataContract.Remote, scheduler: Scheduler, compositeDisposable: CompositeDisposable): GithubDataContract.Repository = GithubRepository(remote, scheduler, compositeDisposable)


    @Provides
    @GithubScope
    fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
}

@Module
class RepoModule{
    @Provides
    @GithubScope
    fun remoteData(service: GithubService): GithubDataContract.Remote = GithubRemote(service)


    @Provides
    @GithubScope
    fun postService(retrofit: Retrofit): GithubService = retrofit.create(GithubService::class.java)

}