package com.korbiak.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<D, E> {
    D toDto(E input);

    E toEntity(D input);

    default List<D> toDtos(List<E> input) {
        return input.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default List<E> toEntities(List<D> input) {
        return input.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
