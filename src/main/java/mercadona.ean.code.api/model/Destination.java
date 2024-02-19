package mercadona.ean.code.api.model;

import java.util.NoSuchElementException;
import java.util.Set;

public enum Destination {
    MercadonaSpain (Set.of(1,2,3,4,5)),
    MercadonaPortugal (Set.of(6)),
    Warehouses (Set.of(8)),
    MercadonaOffices (Set.of(9)),
    Colmenas (Set.of(0));

    private Set<Integer> dest;

    private Destination(final Set<Integer> dest) {
        this.dest = dest;
    }

    public Set<Integer> getDest() {
        return dest;
    }

    public static Destination fromCode(int code) {
        for (Destination dest : Destination.values()) {
            if (dest.getDest().contains(code)) {
                return dest;
            }
        }
        throw new NoSuchElementException("Can't find the destination by code: " + code);
    }
}
