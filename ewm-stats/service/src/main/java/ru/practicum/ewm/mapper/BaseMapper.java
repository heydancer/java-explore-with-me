package ru.practicum.ewm.mapper;

public interface BaseMapper<D, M> {
    M toModel(D d);

    D toDTO(M m);
}