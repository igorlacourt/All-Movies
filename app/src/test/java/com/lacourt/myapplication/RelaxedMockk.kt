package com.lacourt.myapplication

import com.lacourt.myapplication.testExample.Person
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RelaxedMockk {

    /*
        If we don't want to describe the behavior of each method,
     then we can use a relaxed mock. This kind of mock provides
     default values for each function. For example, the String
     return type will return an empty String. Here's a short example

    */
    @Nested
    inner class `Dado o viewmodel`{
        // given
        val person = mockk<Person>(relaxed = true)

        @Test
        fun`Quando chamar getFirstString(), verifica se o resultado é vazio`(){
            // when
            val result = person.getFirstString()

            // then
            assertEquals("", result)
        }
    }
}

