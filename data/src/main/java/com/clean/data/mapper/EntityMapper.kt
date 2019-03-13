package com.clean.data.mapper

interface EntityMapper<D, E> {

    fun mapToEntity(data: D): E

}