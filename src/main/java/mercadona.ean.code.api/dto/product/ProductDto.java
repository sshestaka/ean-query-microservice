package mercadona.ean.code.api.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductDto {
    private long id;
    private int code;
    private String title;
    private String category;
    private String packaging;
    private String rating;
}
