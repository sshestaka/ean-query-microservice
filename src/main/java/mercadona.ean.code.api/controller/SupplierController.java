package mercadona.ean.code.api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.supplier.CreateSupplierRequestDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.dto.supplier.UpdateSupplierRequestDto;
import mercadona.ean.code.api.service.SupplierService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition
@RequiredArgsConstructor
@RequestMapping(value = "/api/suppliers")
@Tag(name = "Suppliers management", description = "Endpoints for managing Suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/{id}")
    @Operation(summary = "Find a supplier by id",
            description = "Get a supplier by id if it's available")
    public SupplierDto findById(@PathVariable Long id) {
        return supplierService.findById(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Find a supplier by code",
            description = "Get a supplier by code if it's available")
    public SupplierDto findByCode(@PathVariable Long code) {
        return supplierService.findByCode(code);
    }

    @GetMapping
    @Operation(summary = "Get all suppliers",
            description = "Get the list of all available suppliers")
    public List<SupplierDto> getAll(Pageable pageable) {
        return supplierService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the supplier by id",
            description = "Delete the supplier by id")
    public void delete(@PathVariable Long id) {
        supplierService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update the supplier by id", description =
            "Update the supplier by id according to the parameters")
    public SupplierDto updateSupplier(@PathVariable Long id,
                              @RequestBody @Valid UpdateSupplierRequestDto updateRequestDto) {
        return supplierService.update(id, updateRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new supplier", description = "Create a new supplier")
    public SupplierDto createSupplier(@RequestBody @Valid CreateSupplierRequestDto requestDto) {
        return supplierService.save(requestDto);
    }
}
