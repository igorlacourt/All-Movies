package com.lacourt.myapplication.domainMappers

interface NullableInputMapper<I, O> {
    fun map(input: I): O
}