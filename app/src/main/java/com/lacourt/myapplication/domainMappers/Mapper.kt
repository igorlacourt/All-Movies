package com.lacourt.myapplication.domainMappers

interface Mapper<I, O> {
    fun map(input: I): O
}