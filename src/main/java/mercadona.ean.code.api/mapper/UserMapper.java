package mercadona.ean.code.api.mapper;

import mercadona.ean.code.api.config.MapperConfig;
import mercadona.ean.code.api.dto.user.UserDto;
import mercadona.ean.code.api.dto.user.UserResponseDto;
import mercadona.ean.code.api.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    UserResponseDto toUserResponse(User user);
}
