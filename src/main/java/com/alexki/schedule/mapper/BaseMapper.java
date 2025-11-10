package com.alexki.schedule.mapper;

public interface BaseMapper<T, M> {

    T toDto(M entity);

    M toEntity(T dto);

}
