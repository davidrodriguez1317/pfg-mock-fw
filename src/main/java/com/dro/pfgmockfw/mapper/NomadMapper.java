package com.dro.pfgmockfw.mapper;

import com.dro.pfgmockfw.model.nomad.RunningJobDto;
import com.dro.pfgmockfw.model.nomad.server.ServerRunningJobDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NomadMapper {
    NomadMapper INSTANCE = Mappers.getMapper(NomadMapper.class);

    RunningJobDto fromServerRunningJobDto(ServerRunningJobDto serverRunningJobDto);


}
