package mercadona.ean.code.api.mapper;

import mercadona.ean.code.api.config.MapperConfig;
import mercadona.ean.code.api.dto.supplier.CreateSupplierRequestDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.model.Supplier;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface SupplierMapper {
    SupplierDto toDto(Supplier supplier);
    Supplier toModel(CreateSupplierRequestDto supplierRequestDto);
}
