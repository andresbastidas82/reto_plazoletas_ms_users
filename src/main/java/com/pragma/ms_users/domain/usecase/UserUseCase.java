package com.pragma.ms_users.domain.usecase;

import com.pragma.ms_users.domain.api.IUserServicePort;
import com.pragma.ms_users.domain.exception.AdultException;
import com.pragma.ms_users.domain.model.User;
import com.pragma.ms_users.domain.model.enums.RoleEnum;
import com.pragma.ms_users.domain.spi.IPasswordEncoderServicePort;
import com.pragma.ms_users.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

import static com.pragma.ms_users.domain.utils.Constants.OVER_18_YEARS_OLD;

@Service
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderServicePort passwordEncoderServicePort;

    @Override
    public User saveUser(User user, RoleEnum role) {
        if(RoleEnum.ROLE_OWNER.equals(role) && Period.between(user.getBirthDate(), LocalDate.now()).getYears() < OVER_18_YEARS_OLD) {
            throw new AdultException();
        }
        user.setPassword(passwordEncoderServicePort.passwordEncrypt(user.getPassword()));
        user.setRole(role);
        return userPersistencePort.saveUser(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userPersistencePort.getUserById(userId);
    }
}
