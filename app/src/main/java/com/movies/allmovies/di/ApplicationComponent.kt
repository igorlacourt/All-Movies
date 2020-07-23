package com.movies.allmovies.di

import android.content.Context
import com.movies.allmovies.di.subcomponent.MainComponent
import com.movies.allmovies.di.subcomponent.MainModule
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    DataModule::class,
    ViewModelBuilderModule::class,
    SubcomponentsModule::class,
    MainModule::class
])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    fun mainComponent(): MainComponent.Factory
}

@Module(subcomponents = [MainComponent::class])
object SubcomponentsModule