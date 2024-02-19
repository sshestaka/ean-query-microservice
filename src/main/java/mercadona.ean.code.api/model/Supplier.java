package mercadona.ean.code.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE suppliers SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "suppliers")
@Accessors(chain = true)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private int code;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String eban;
    @Column(nullable = false)
    private String address;
    @Column(unique = true, nullable = false, name = "phone_number")
    private String phoneNumber;
    private String description;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
