package mercadona.ean.code.api.service;

import mercadona.ean.code.api.dto.supplier.SupplierDto;
import mercadona.ean.code.api.mapper.ProductMapper;
import mercadona.ean.code.api.mapper.SupplierMapper;
import mercadona.ean.code.api.repository.ProductRepository;
import mercadona.ean.code.api.repository.SupplierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EanServiceTest {
    private static final String EAN_WITH_NON_EXISTING_SUPPLIER_ID = "1234567459053";
    private static final String EAN_WITH_NON_EXISTING_PRODUCT_ID = "8437008123453";
    private static final String NON_EXISTING_SUPPLIER_ID = "1234567";
    private static final String NON_EXISTING_PRODUCT_ID = "12345";
    private static final String EXISTING_PRODUCT_ID = "45905";
    private static final String EXISTING_DESTINATION_ID = "3";
    @InjectMocks
    private EanServiceImpl eanService;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SupplierMapper supplierMapper;
    @Mock
    private ProductMapper productMapper;

    //In this case, unit tests do not make much sense.
    //This is just a demonstration of working with Mockito
    @Test
    @DisplayName("Check if findByEan() method throws correct "
            + "exception with non existing Supplier code")
    public void findByEan_GivenNonExistingSupplierNumber_ShouldThrowException() {
        Mockito.when(supplierRepository.getByCode(Long.valueOf(NON_EXISTING_SUPPLIER_ID)))
                .thenThrow(new RuntimeException("Can't find the supplier by code: "
                        + NON_EXISTING_SUPPLIER_ID));
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> eanService.findByEan(EAN_WITH_NON_EXISTING_SUPPLIER_ID)
        );
        String expected = "Can't find the supplier by code: " + NON_EXISTING_SUPPLIER_ID;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    private SupplierDto getSupplier1Dto() {
        return new SupplierDto()
                .setId(1L)
                .setCode(8437008)
                .setName("Supplier-1")
                .setEban("EBAN:0001")
                .setAddress("ADDRESS-01")
                .setPhoneNumber("+34111222333")
                .setDescription("Description-1");
    }
}
