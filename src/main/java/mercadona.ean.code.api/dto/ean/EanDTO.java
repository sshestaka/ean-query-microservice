package mercadona.ean.code.api.dto.ean;

import lombok.Data;
import lombok.experimental.Accessors;
import mercadona.ean.code.api.dto.product.ProductDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.model.Destination;

@Data
@Accessors(chain = true)
public class EanDTO {
    SupplierDto supplier;
    ProductDto product;
    Destination destination;
}
