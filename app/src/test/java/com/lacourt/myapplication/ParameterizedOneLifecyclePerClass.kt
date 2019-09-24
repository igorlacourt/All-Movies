package com.lacourt.myapplication

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParameterizedOneLifecyclePerClass {

    /*
    To use a non-static method, you need to change the life cycle
    of the test instance; more precisely, create one instance of
    the test for the class (@TestInstance(TestInstance.Lifecycle.PER_CLASS)),
    instead of one instance per method, as is done by default.
    */
    @ParameterizedTest
    @MethodSource("intProvider")
    fun `Test with custom arguments provider`(argument: Int) {
        assertNotNull(argument)
    }

    fun intProvider(): Stream<Int> = Stream.of(0, 42, 9000)
}