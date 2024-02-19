package mercadona.ean.code.api.service;

import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.product.CreateProductRequestDto;
import mercadona.ean.code.api.dto.product.ProductDto;
import mercadona.ean.code.api.dto.product.UpdateProductRequestDto;
import mercadona.ean.code.api.mapper.ProductMapper;
import mercadona.ean.code.api.model.Product;
import mercadona.ean.code.api.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto save(CreateProductRequestDto requestDto) {
        Product product = productMapper.toModel(requestDto);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(Long id, UpdateProductRequestDto updateRequestDto) {
        Product updatedProduct = productRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find the product by id: " + id));
        updateProduct(updatedProduct, updateRequestDto);
        return productMapper.toDto(productRepository.save(updatedProduct));
    }

    @Override
    public List<ProductDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductDto findById(Long id) {
        return productMapper.toDto(productRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find the product by id: " + id)
        ));
    }

    @Override
    public ProductDto findByCode(Long code) {
        return productMapper.toDto(productRepository.findByCode(code).orElseThrow(() ->
                new NoSuchElementException("Can't find the product by code: " + code)
        ));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private void updateProduct(Product updatedProduct, UpdateProductRequestDto updateRequestDto) {
        if (updateRequestDto.getCode() != 0) {
            updatedProduct.setCode(updateRequestDto.getCode());
        }
        if (updateRequestDto.getTitle() != null) {
            updatedProduct.setTitle(updateRequestDto.getTitle());
        }
        if (updateRequestDto.getCategory() != null) {
            updatedProduct.setCategory(updateRequestDto.getCategory());
        }
        if (updateRequestDto.getPackaging() != null) {
            updatedProduct.setPackaging(updateRequestDto.getPackaging());
        }
        if (updateRequestDto.getRating() != null) {
            updatedProduct.setRating(updateRequestDto.getRating());
        }
    }
}
