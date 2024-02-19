package mercadona.ean.code.api.service;

import mercadona.ean.code.api.dto.supplier.CreateSupplierRequestDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.dto.supplier.UpdateSupplierRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SupplierService {
    SupplierDto save(CreateSupplierRequestDto requestDto);
    SupplierDto update(Long id, UpdateSupplierRequestDto updateRequestDto);
    List<SupplierDto> getAll(Pageable pageable);
    SupplierDto findById(Long id);
    SupplierDto findByCode(Long code);
    void deleteById(Long id);
}
