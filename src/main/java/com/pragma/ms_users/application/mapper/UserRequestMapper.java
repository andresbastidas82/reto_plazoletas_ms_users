package com.pragma.ms_users.application.mapper;

import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.DocumentTypeEnum;
import com.pragma.ms_users.infrastructure.exception.DocumentTypeNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import static com.pragma.ms_users.application.utils.Constants.DOCUMENT_TYPE_CC;
import static com.pragma.ms_users.application.utils.Constants.DOCUMENT_TYPE_CE;
import static com.pragma.ms_users.application.utils.Constants.DOCUMENT_TYPE_NIT;
import static com.pragma.ms_users.application.utils.Constants.DOCUMENT_TYPE_PASSPORT;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserRequestMapper {

    @Mapping(target = "documentType", qualifiedByName = "getDocument")
    User toUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);


    @Named("getDocument")
    static DocumentTypeEnum getDocument(String documentType) {
        return switch (documentType) {
            case DOCUMENT_TYPE_CC ->
                    DocumentTypeEnum.CITIZENSHIP_CARD;
            case DOCUMENT_TYPE_CE ->
                    DocumentTypeEnum.FOREIGNER_IDENTITY_CARD;
            case DOCUMENT_TYPE_PASSPORT ->
                    DocumentTypeEnum.PASSPORT;
            case DOCUMENT_TYPE_NIT ->
                    DocumentTypeEnum.NIT;
            default -> throw new DocumentTypeNotFoundException();
        };
    }
}
