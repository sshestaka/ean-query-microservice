package mercadona.ean.code.api.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mercadona.ean.code.api.dto.ean.EanDTO;
import mercadona.ean.code.api.service.EanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition
@RequiredArgsConstructor
@RequestMapping(value = "/api/eans")
@Tag(name = "EAN numbers management", description = "Endpoints for managing EAN numbers")
public class EanController {
    private final EanService eanService;

    @GetMapping("/{ean}")
    @Operation(summary = "Find the information by EAN code",
            description = "Get the information by EAN code if it's available")
    public EanDTO findByEan(@PathVariable String ean) {
        return eanService.findByEan(ean);
    }
}
