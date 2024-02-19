package mercadona.ean.code.api.dto.product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateProductRequestDto {
    @NotNull
    @Column(unique = true)
    private int code;
    @NotNull
    @Column(unique = true)
    private String title;
    @NotNull
    private String category;
    @NotNull
    private String packaging;
    private String rating;
}
