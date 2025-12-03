package com.pragma.ms_users.infrastructure.out.jpa.mapper;

import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.infrastructure.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserEntityMapper {

    UserEntity toUserEntity(User user);

    @Mapping(target = "password", ignore = true)
    User toUser(UserEntity userEntity);

}
