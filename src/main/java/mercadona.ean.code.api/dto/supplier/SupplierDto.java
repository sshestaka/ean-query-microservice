package mercadona.ean.code.api.dto.supplier;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SupplierDto {
    private long id;
    private int code;
    private String name;
    private String eban;
    private String address;
    private String phoneNumber;
    private String description;
}
