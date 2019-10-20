package com.lacourt.myapplication.di

import androidx.lifecycle.ViewModel
import com.lacourt.myapplication.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchViewModelsModule{
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindAuthViewModel(viewModel: SearchViewModel): ViewModel
}