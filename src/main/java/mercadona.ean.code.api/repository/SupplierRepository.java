package mercadona.ean.code.api.repository;

import mercadona.ean.code.api.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByCode(Long code);
    default Supplier getByCode(Long code) {
        return findByCode(code).orElseThrow(() ->
                new NoSuchElementException("Can't find the supplier by code: "
                        + code));
    }
}
