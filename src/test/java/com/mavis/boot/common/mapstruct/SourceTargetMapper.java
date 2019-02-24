package com.mavis.boot.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SourceTargetMapper {

    SourceTargetMapper MAPPER = Mappers.getMapper(SourceTargetMapper.class);

    @Mapping(source = "test", target = "testing")
    Target toTarget(Source s);
}