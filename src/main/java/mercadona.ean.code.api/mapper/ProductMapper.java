package mercadona.ean.code.api.mapper;

import mercadona.ean.code.api.config.MapperConfig;
import mercadona.ean.code.api.dto.product.CreateProductRequestDto;
import mercadona.ean.code.api.dto.product.ProductDto;
import mercadona.ean.code.api.model.Product;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toModel(CreateProductRequestDto productRequestDto);
}
