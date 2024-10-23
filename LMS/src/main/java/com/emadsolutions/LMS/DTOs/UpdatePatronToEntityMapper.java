package com.emadsolutions.LMS.DTOs;
import com.emadsolutions.LMS.patron.Patron;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdatePatronToEntityMapper {
    UpdatePatronToEntityMapper INSTANCE = Mappers.getMapper(UpdatePatronToEntityMapper.class);

    @Mapping(target = "id", ignore = true) // Prevent mapping of id
    @Mapping(target = "borrowingRecords", ignore = true) // Prevent mapping of borrowingRecords
    Patron toEntity(UpdatePatronToEntity entity);

}


