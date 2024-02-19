package mercadona.ean.code.api.service;

import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.user.UserRegistrationRequestDto;
import mercadona.ean.code.api.dto.user.UserResponseDto;
import mercadona.ean.code.api.exception.RegistrationException;
import mercadona.ean.code.api.mapper.UserMapper;
import mercadona.ean.code.api.model.Role;
import mercadona.ean.code.api.model.User;
import mercadona.ean.code.api.repository.RoleRepository;
import mercadona.ean.code.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("User with email " + request.getEmail()
                    + " is already exist.");
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDepartment(request.getDepartment());
        user.setRoles(Collections.singleton(roleRepository
                .findByRoleName(Role.RoleName.ROLE_USER)));
        User savedUser = userRepository.save(user);

        return userMapper.toUserResponse(savedUser);
    }
}
