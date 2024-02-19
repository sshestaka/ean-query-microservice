package mercadona.ean.code.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EanDetails {
    private Long supplierNumber;
    private Long productNumber;
    private Long destinationNumber;
}
