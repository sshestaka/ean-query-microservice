package mercadona.ean.code.api.service;

import mercadona.ean.code.api.dto.ean.EanDTO;

public interface EanService {
    EanDTO findByEan(String ean);
}
