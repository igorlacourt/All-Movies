package com.lacourt.myapplication

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lacourt.myapplication.database.MyListDao
import com.lacourt.myapplication.repository.HomeRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.Context
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.ContextWrapper
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.InjectMocks



@RunWith(MockitoJUnitRunner::class)
class HomeRepositoryTest {
    //Should use MyListDao as dependency injected in the repository, so I'd just need to mock it
//    @Mock
//    lateinit var myListDao: MyListDao

    @Spy
    lateinit var app: Application

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var repository: HomeRepository? = null

    @Before
    fun setUp(){
        repository = HomeRepository(app)
        //Not sure if it's needed
        repository!!.listsOfMovies.observeForever {  }
    }

    @Test
    fun `when fetch movies is called, livedata objects receive the expected values`(){
        // Write this test using idling resource to check the live data after all operations finish.
//        Mockito.`when`(repository!!.justForTesting()).thenReturn(repository!!.listsOfMovies)
    }
}