package mercadona.ean.code.api.service;

import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.supplier.CreateSupplierRequestDto;
import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.dto.supplier.UpdateSupplierRequestDto;
import mercadona.ean.code.api.mapper.SupplierMapper;
import mercadona.ean.code.api.model.Supplier;
import mercadona.ean.code.api.repository.SupplierRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService{
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierDto save(CreateSupplierRequestDto requestDto) {
        Supplier supplier = supplierMapper.toModel(requestDto);
        return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public SupplierDto update(Long id, UpdateSupplierRequestDto updateRequestDto) {
        Supplier updatedSupplier = supplierRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find the supplier by id: " + id));
        updateSupplier(updatedSupplier, updateRequestDto);
        return supplierMapper.toDto(supplierRepository.save(updatedSupplier));
    }

    @Override
    public List<SupplierDto> getAll(Pageable pageable) {
        return supplierRepository.findAll(pageable).stream()
                .map(supplierMapper::toDto)
                .toList();
    }

    @Override
    public SupplierDto findById(Long id) {
        return supplierMapper.toDto(supplierRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find the supplier by id: " + id)
        ));
    }

    @Override
    public SupplierDto findByCode(Long code) {
        return supplierMapper.toDto(supplierRepository.findByCode(code).orElseThrow(() ->
                new NoSuchElementException("Can't find the supplier by code: " + code)
        ));
    }

    @Override
    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

    private void updateSupplier(Supplier updatedSupplier, UpdateSupplierRequestDto updateRequestDto) {
        if (updateRequestDto.getCode() != 0) {
            updatedSupplier.setCode(updateRequestDto.getCode());
        }
        if (updateRequestDto.getName() != null) {
            updatedSupplier.setName(updateRequestDto.getName());
        }
        if (updateRequestDto.getEban() != null) {
            updatedSupplier.setEban(updateRequestDto.getEban());
        }
        if (updateRequestDto.getAddress() != null) {
            updatedSupplier.setAddress(updateRequestDto.getAddress());
        }
        if (updateRequestDto.getPhoneNumber() != null) {
            updatedSupplier.setPhoneNumber(updateRequestDto.getPhoneNumber());
        }
        if (updateRequestDto.getDescription() != null) {
            updatedSupplier.setDescription(updateRequestDto.getDescription());
        }
    }
}
