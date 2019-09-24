package com.lacourt.myapplication

import com.lacourt.myapplication.testExample.Person
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

import java.time.LocalDate
import java.util.stream.Stream

internal class NestedTestExample {
    /*
        JUnit 5 allows you to write nested tests for greater visibility and highlighting
     the relationships between them. Let's create an example using the Person class and
     our own provider of test arguments, returning a stream of Person objects.
     */

    @Nested
    inner class `Check age of person` {
        @ParameterizedTest
        @ArgumentsSource(PersonProvider::class)
        fun `Check age greater or equals 18`(person: Person) {
            assertTrue(person.age >= 18)
        }

        @ParameterizedTest
        @ArgumentsSource(PersonProvider::class)
        fun `Check birth date is after 1950`(person: Person) {
            assertTrue(LocalDate.of(1950, 12, 31).isBefore(person.birthDate))
        }
    }

    @Nested
    inner class `Check name of person` {

        @ParameterizedTest
        @ArgumentsSource(PersonProvider::class)
        fun `Check first name length is 4`(person: Person) {
            assertEquals(4, person.firstName.length)
        }

    }

    internal class PersonProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> = Stream.of(
            Person("John", "Doe", LocalDate.of(1969, 5, 20)),
            Person("Jane", "Smith", LocalDate.of(1997, 11, 21)),
            Person("Ivan", "Ivanov", LocalDate.of(1994, 2, 12))
        ).map { Arguments.of(it) }
    }

}