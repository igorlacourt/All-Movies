package com.lacourt.myapplication

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.RepetitionInfo
import org.junit.jupiter.api.TestInfo

class RepeatableTest {

    @RepeatedTest(10)
    fun `Repeat test`() {

    }

    /*
    * It is possible to customize the displayed test name:
    * */
    @RepeatedTest(10, name = "{displayName} {currentRepetition} of {totalRepetitions}")
    fun `Repeat test customized name`() {

    }

    /*
    *   Access to information about the current test and the group of
    * repeated tests can be obtained through the corresponding objects
    * */
    @RepeatedTest(5)
    fun `Repeated test with repetition info and test info`(
        repetitionInfo: RepetitionInfo,
        testInfo: TestInfo
    ) {
        assertEquals(5, repetitionInfo.totalRepetitions)
        val testDisplayNameRegex = """repetition \d of 5""".toRegex()
        assertTrue(testInfo.displayName.matches(testDisplayNameRegex))
    }
}

