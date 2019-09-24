package com.lacourt.myapplication

import com.lacourt.myapplication.testExample.Person
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SpyTest {
    @Nested
    inner class `given service spy` {
        //        @SpyK
//        lateinit var viewModel: HomeViewModel
// ou então:
        val person = spyk(Person("João", "Da Silva", LocalDate.of(1950, 12, 31)))

        @BeforeEach
        fun init() {
            every { person.getFirstString()} returns "String"
        }

        @Test
        fun `When checking mocked method, then assert that it matches given movie`() {
            val firstResult = person.getFirstString()
            Assertions.assertEquals("String", firstResult)
        }

        @Test
        fun `When checking NOT mocked method, then assert that it matches given movie`() {
            val secondResult = person.getSecondString()
            person.getSecondString()
//            verify { person.getSecondString() }
            Assertions.assertEquals("Second", secondResult)
        }
    }
}
