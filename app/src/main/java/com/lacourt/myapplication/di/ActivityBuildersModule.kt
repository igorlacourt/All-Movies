package com.lacourt.myapplication.di

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
//        (
//        modules = [SearchViewModelsModule::class, DiActivityModule::class]
//    )
    abstract fun contributeAuthActivity(): DiActivity

//
//    @MainScope
//    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class, MainViewModelsModule::class, MainModule::class])
//    internal abstract fun contributeMainActivity(): MainActivity

}
