package com.alexki.tasklist.mapper;

public interface BaseMapper<T, M> {

    T toDto(M entity);

    M toEntity(T dto);

}
