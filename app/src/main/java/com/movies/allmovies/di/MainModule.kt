package com.movies.allmovies.di

import androidx.lifecycle.ViewModel
import com.movies.allmovies.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonViewModel::class)
    fun bindPersonViewModel(viewModel: PersonViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MyListViewModel::class)
    fun bindMyListViewModel(viewModel: MyListViewModel): ViewModel
}