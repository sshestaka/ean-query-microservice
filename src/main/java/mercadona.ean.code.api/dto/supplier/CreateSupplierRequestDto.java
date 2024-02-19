package mercadona.ean.code.api.dto.supplier;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateSupplierRequestDto {
    @NotNull
    @Column(unique = true)
    private int code;
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    @Column(unique = true)
    private String eban;
    @NotNull
    private String address;
    @NotNull
    private String phoneNumber;
    private String description;
}
