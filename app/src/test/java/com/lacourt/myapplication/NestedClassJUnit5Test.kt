package com.lacourt.myapplication

import com.lacourt.myapplication.testExample.Person
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate

class NestedClassJUnit5Test {

    @TestFactory
    fun `Run multiple tests`(): Collection<DynamicTest> {
        val persons = listOf(
            Person("John", "Doe", LocalDate.of(1969, 5, 20)),
            Person("Jane", "Smith", LocalDate.of(1997, 11, 21)),
            Person("Ivan", "Ivanov", LocalDate.of(1994, 2, 12))
        )

        val minAgeFilter = 18

        return persons.map {
            dynamicTest("Check person $it on age greater or equals $minAgeFilter") {
                assertTrue(it.age >= minAgeFilter)
            }
        }.toList()

    }

    private var someVar: Int? = null

    @BeforeEach
    fun `Reset some var`() {
        someVar = 0
        print("@BeforeEach called.")
    }

    @TestFactory
    fun `Test factory`(): Collection<DynamicTest> {
        val ints = 0..5

        return ints.map {
            dynamicTest("Test №$it incrementing some var") {
                someVar = someVar?.inc()
                print("$someVar ")
            }
        }.toList()
    }

}

