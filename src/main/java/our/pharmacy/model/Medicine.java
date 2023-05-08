package our.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_manufacturing", nullable = false)
    private Boolean isManufacturing;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "manufacturing_technology")
    private String manufacturingTechnology;

    @ManyToOne
    @JoinColumn(name = "medicine_type_id", nullable = false)
    private MedicineType medicineType;

    @OneToOne
    @PrimaryKeyJoinColumn(name="id")
    private MedicineDerived derived;

    @OneToMany(mappedBy = "medicine")
    @JsonIgnore
    private List<MedicineItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturingTechnology() {
        return manufacturingTechnology;
    }

    public void setManufacturingTechnology(String manufacturingTechnology) {
        this.manufacturingTechnology = manufacturingTechnology;
    }

    public MedicineType getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(MedicineType medicineType) {
        this.medicineType = medicineType;
    }

    public List<MedicineItem> getItems() {
        return items;
    }

    public void setItems(List<MedicineItem> items) {
        this.items = items;
    }

    public MedicineDerived getDerived() {
        return derived;
    }

    public void setDerived(MedicineDerived derived) {
        this.derived = derived;
    }

    public Boolean getIsManufacturing() {
        return isManufacturing;
    }

    public void setIsManufacturing(Boolean manufacturing) {
        isManufacturing = manufacturing;
    }
}