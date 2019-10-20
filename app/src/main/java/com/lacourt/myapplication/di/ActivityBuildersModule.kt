package com.lacourt.myapplication.di

import com.lacourt.myapplication.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [SearchViewModelsModule::class])
    internal abstract fun contributeAuthActivity(): SearchFragment

//
//    @MainScope
//    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class, MainViewModelsModule::class, MainModule::class])
//    internal abstract fun contributeMainActivity(): MainActivity

}
