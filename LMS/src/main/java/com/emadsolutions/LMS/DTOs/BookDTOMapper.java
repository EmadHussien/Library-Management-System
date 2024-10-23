package com.emadsolutions.LMS.DTOs;
import com.emadsolutions.LMS.book.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookDTOMapper {
    BookDTOMapper INSTANCE = Mappers.getMapper(BookDTOMapper.class);
    BookToDTO toDTO(Book entity);

}


