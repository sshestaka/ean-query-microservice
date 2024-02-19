package mercadona.ean.code.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import mercadona.ean.code.api.validation.FieldsValueMatch;

@Data
@Accessors(chain = true)
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "repeatPassword",
                message = "Passwords do not match!"
        )
})
public class UserRegistrationRequestDto {
    @NotEmpty
    @Size(min = 4, max = 50)
    @Email
    private String email;
    @NotEmpty
    @Size(min = 4, max = 50)
    private String password;
    @NotEmpty
    @Size(min = 4, max = 50)
    private String repeatPassword;
    private String firstName;
    private String lastName;
    private String department;
}
