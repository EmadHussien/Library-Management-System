package com.emadsolutions.LMS.DTOs;

import com.emadsolutions.LMS.book.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateBookToEntityMapper {
    UpdateBookToEntityMapper INSTANCE = Mappers.getMapper(UpdateBookToEntityMapper.class);
    Book toEntity(UpdateBookToEntity entity);
}

