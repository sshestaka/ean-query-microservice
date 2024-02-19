package mercadona.ean.code.api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.product.CreateProductRequestDto;
import mercadona.ean.code.api.dto.product.ProductDto;
import mercadona.ean.code.api.dto.product.UpdateProductRequestDto;
import mercadona.ean.code.api.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition
@RequiredArgsConstructor
@RequestMapping(value = "/api/products")
@Tag(name = "Products management", description = "Endpoints for managing Products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Find the product by id",
            description = "Get the product by id if it's available")
    public ProductDto findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Find the product by code",
            description = "Get the product by code if it's available")
    public ProductDto findByCode(@PathVariable Long code) {
        return productService.findByCode(code);
    }

    @GetMapping
    @Operation(summary = "Get all products",
            description = "Get the list of all available productrs")
    public List<ProductDto> getAll(Pageable pageable) {
        return productService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the product by id", description = "Delete the product by id")
    public void delete(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update the product by id", description =
            "Update the product by id according to the parameters")
    public ProductDto updateProduct(@PathVariable Long id,
                                  @RequestBody @Valid UpdateProductRequestDto updateRequestDto) {
        return productService.update(id, updateRequestDto);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a new product")
    public ProductDto createProduct(@RequestBody @Valid CreateProductRequestDto requestDto) {
        return productService.save(requestDto);
    }
}
