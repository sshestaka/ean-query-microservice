package mercadona.ean.code.api.repository;

import mercadona.ean.code.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(Long code);

    default Product getByCode(Long code) {
        return findByCode(code).orElseThrow(() ->
                new NoSuchElementException("Can't find the product by code: "
                        + code));
    }
}
