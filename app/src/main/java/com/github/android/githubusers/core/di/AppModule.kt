package com.github.android.githubusers.core.di

import android.content.Context
import com.github.android.githubusers.core.networking.AppScheduler
import com.github.android.githubusers.core.networking.Scheduler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {
    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun scheduler(): Scheduler {
        return AppScheduler()
    }
}