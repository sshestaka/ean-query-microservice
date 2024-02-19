package mercadona.ean.code.api.service;

import mercadona.ean.code.api.dto.user.UserRegistrationRequestDto;
import mercadona.ean.code.api.dto.user.UserResponseDto;
import mercadona.ean.code.api.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;
}
