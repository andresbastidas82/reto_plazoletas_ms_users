package com.pragma.ms_users.application.handler;

import com.pragma.ms_users.application.dto.UserRequest;
import com.pragma.ms_users.application.dto.UserResponse;
import com.pragma.ms_users.application.mapper.UserRequestMapper;
import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.infrastructure.exception.AdultException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

import static com.pragma.ms_users.application.utils.Constants.OVER_18_YEARS_OLD;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final UserRequestMapper userRequestMapper;
    private final IUserServicePort userServicePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserResponse saveUserTypeOwner(UserRequest userRequest) {
        if(Period.between(userRequest.getBirthDate(), LocalDate.now()).getYears() < OVER_18_YEARS_OLD) {
            throw new AdultException();
        }
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        User user = userServicePort.saveUser(userRequestMapper.toUser(userRequest));
        return userRequestMapper.toUserResponse(user);
    }
}
