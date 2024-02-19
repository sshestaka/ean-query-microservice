package mercadona.ean.code.api.service;

import mercadona.ean.code.api.dto.product.CreateProductRequestDto;
import mercadona.ean.code.api.dto.product.ProductDto;
import mercadona.ean.code.api.dto.product.UpdateProductRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDto save(CreateProductRequestDto requestDto);
    ProductDto update(Long id, UpdateProductRequestDto updateRequestDto);
    List<ProductDto> getAll(Pageable pageable);
    ProductDto findById(Long id);
    ProductDto findByCode(Long id);
    void deleteById(Long id);
}
