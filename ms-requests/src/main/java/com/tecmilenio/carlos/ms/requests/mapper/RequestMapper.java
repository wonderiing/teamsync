package com.tecmilenio.carlos.ms.requests.mapper;

import com.tecmilenio.carlos.ms.requests.dto.CreateRequestDto;
import com.tecmilenio.carlos.ms.requests.dto.RequestDto;
import com.tecmilenio.carlos.ms.requests.entities.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {

    RequestDto toDto(Request request);

    Request toEntity(RequestDto requestDto);

    @Mapping(target = "idRequest", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Request toEntity(CreateRequestDto createRequestDto);

    List<RequestDto> toDtoList(List<Request> requests);

    List<Request> toEntityList(List<RequestDto> requestDtos);
}
