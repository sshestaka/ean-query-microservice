package mercadona.ean.code.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record UserLoginRequestDto(
        @NotEmpty
        @Size(min = 4, max = 50)
        @Email
        String email,

        @NotEmpty
        @Size(min = 4, max = 50)
        String password
) {
}
