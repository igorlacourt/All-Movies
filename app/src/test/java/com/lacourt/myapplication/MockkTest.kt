package com.lacourt.myapplication

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.lacourt.myapplication.model.Movie
import com.lacourt.myapplication.testExample.Person
import com.lacourt.myapplication.ui.home.HomeViewModel
import io.mockk.*
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

class MockkTest {
    val movie = Movie(
        false,
        "backdrop_path",
        arrayListOf("10", "11", "12"),
        arrayListOf("Action", "Adventure", "Comedy"),
        10,
        "English",
        "Title",
        "Overview",
        10.0,
        "poster_path",
        "release_date",
        "title",
        true,
        10.0,
        1000
    )

    val emptyMovie = Movie(
        null,
        null,
        null,
        null,
        11,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    @Nested
    inner class `Given the viewmodel active` {
        // Given
        val viewmodel = mockk<HomeViewModel>()

        @BeforeEach
        fun initViewModel() {
            // Given
            every { viewmodel.getMovieById(10) } returns movie
        }

        @Test
        fun `When performing database getMovieById(), check if movie matches`() {

            // When
            val result = viewmodel.getMovieById(10)

            // Then
            verify {
                viewmodel.getMovieById(10)
                assertEquals(movie, result)
            }
        }

        @AfterEach
        fun `clear mocks`() {
            clearMocks(viewmodel)
        }
    }

}

