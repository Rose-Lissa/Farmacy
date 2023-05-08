package our.pharmacy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "shopping_cart_item_derived")
@Immutable
public class ShoppingCartItemDerived {
    @Id
    private Long id;

    @Column(name = "manufacturing_quantity")
    private Integer quantityInManufacturing;

    @Column(name = "external_quantity")
    private Integer quantityInExternal;

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public Integer getQuantityInManufacturing() {
        return quantityInManufacturing;
    }

    public void setQuantityInManufacturing(Integer quantityInManufacturing) {
        this.quantityInManufacturing = quantityInManufacturing;
    }

    public Integer getQuantityInExternal() {
        return quantityInExternal;
    }

    public void setQuantityInExternal(Integer quantityInExternal) {
        this.quantityInExternal = quantityInExternal;
    }
}
