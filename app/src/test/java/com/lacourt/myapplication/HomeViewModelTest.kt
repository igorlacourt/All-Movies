package com.lacourt.myapplication

import android.app.Application
import com.lacourt.myapplication.viewmodel.HomeViewModel
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
//
//        viewModel.refresh()
//
//        viewModel.setNewValue("foo")
//
//        // This works but can be improved.
//        viewModel.liveData2.observeForever { }
//
//        // Passes because it's not a result of a Transformation:
//        assertEquals(viewModel.liveData1.value, "foo")
//
//        // Passes because now it's being observed:
//        assertEquals(viewModel.liveData2.value, "FOO")
    }
}