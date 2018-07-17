package com.github.android.githubusers

import android.app.Application
import com.github.android.githubusers.core.di.AppModule
import com.github.android.githubusers.core.di.CoreComponent
import com.github.android.githubusers.core.di.DaggerCoreComponent

class GithubApplication: Application(){
    companion object {
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDI()
    }


    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }
}