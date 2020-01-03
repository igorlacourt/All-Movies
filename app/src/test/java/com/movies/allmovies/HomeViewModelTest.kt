package com.movies.allmovies

import android.app.Application
import com.movies.allmovies.network.Resource
import com.movies.allmovies.viewmodel.HomeViewModel
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @Mock
    lateinit var application: Application

    val viewModel by lazy { HomeViewModel(application) }

    @Test
    fun isLiveDataEmitting_observeForever() {

        viewModel.listsOfMovies!!.observeForever {  }

        viewModel.refresh()

        // Passes because now it's being observed:
        assertEquals(viewModel.listsOfMovies!!.value!!.status, Resource.Status.SUCCESS)
    }
}