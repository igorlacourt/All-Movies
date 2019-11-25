package com.lacourt.myapplication

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lacourt.myapplication.repository.HomeRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.InjectMocks



@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest {
    @Mock
    lateinit var app: Application

//    @Spy
    lateinit var repository: HomeRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @InjectMocks
    private val app2 = Application()

    @Before
    fun setMocks(){
        var homeRepository = HomeRepository(app)
        repository = Mockito.spy(homeRepository)
    }

    @Test
    fun `when fetch movies is called, livedata objects receive the expected values`(){
        Mockito.`when`(repository.justForTesting()).thenReturn(repository.listsOfMovies)
    }
}