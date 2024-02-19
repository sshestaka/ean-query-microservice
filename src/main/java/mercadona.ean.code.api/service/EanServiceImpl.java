package mercadona.ean.code.api.service;

import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.ean.EanDTO;
import mercadona.ean.code.api.mapper.ProductMapper;
import mercadona.ean.code.api.mapper.SupplierMapper;
import mercadona.ean.code.api.model.EanDetails;
import mercadona.ean.code.api.repository.ProductRepository;
import mercadona.ean.code.api.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import static mercadona.ean.code.api.model.Destination.fromCode;

@Service
@RequiredArgsConstructor
public class EanServiceImpl implements EanService {
    private static final int BASE_POWER = 10;
    private static final int POWER_FOR_DESTINATION = 1;
    private static final int POWER_FOR_PRODUCT = 5;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final SupplierMapper supplierMapper;
    private final ProductMapper productMapper;

    @Override
    public EanDTO findByEan(String ean) {
        Long eanCode =  eanValidation(ean);
        EanDetails detailsFromEan = getDetailsFromEan(eanCode);
        EanDTO eanDTO = new EanDTO();

        eanDTO.setSupplier(supplierMapper.toDto(
                supplierRepository.getByCode(detailsFromEan.getSupplierNumber())));

        eanDTO.setProduct(productMapper.toDto(
                productRepository.getByCode(detailsFromEan.getProductNumber())));

        eanDTO.setDestination(fromCode(Math.toIntExact(detailsFromEan.getDestinationNumber())));
        return eanDTO;
    }

    //move to controller
    private Long eanValidation(String ean) {
        if (ean.matches("\\d{13}")) {
            return Long.valueOf(ean);
        }
        throw new IllegalArgumentException("EAN must be 13 digits long");
    }

    //move to util package
    private EanDetails getDetailsFromEan(Long ean) {
        EanDetails eanDetails = new EanDetails();

        eanDetails.setDestinationNumber(ean % (long) Math.pow(10, POWER_FOR_DESTINATION)); // get last digit
        ean /= (long) Math.pow(10, POWER_FOR_DESTINATION);

        eanDetails.setProductNumber(ean % (long) Math.pow(10, POWER_FOR_PRODUCT)); // get next 5 digits
        ean /= (long) Math.pow(10, POWER_FOR_PRODUCT);

        eanDetails.setSupplierNumber(ean); // remaining digits are the first 7

        return eanDetails;
    }
}
