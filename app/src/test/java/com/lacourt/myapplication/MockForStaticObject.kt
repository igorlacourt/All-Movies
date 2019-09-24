package com.lacourt.myapplication

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/* Este teste não está rodando */

class MockForStaticObject  {
    @BeforeEach
    fun beforeTests() {
        mockkObject(MockObj)
        every { MockObj.add(1,2) } returns 55
    }

    @Test
    fun willUseMockBehaviour() {
        Assertions.assertEquals(55, MockObj.add(1, 2))
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
        // or unmockkObject(MockObj)
    }

}

object MockObj {
    fun add(a: Int, b: Int) = a + b
}