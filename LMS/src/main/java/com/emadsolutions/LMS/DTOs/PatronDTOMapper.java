package com.emadsolutions.LMS.DTOs;

import com.emadsolutions.LMS.patron.Patron;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatronDTOMapper {


    PatronDTOMapper INSTANCE = Mappers.getMapper(PatronDTOMapper.class);
    PatronToDTO toDTO(Patron entity);
}
