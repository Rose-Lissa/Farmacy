package our.pharmacy.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stage", nullable = false)
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStage stage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    private List<MedicineItem> medicineItems = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrderStage getStage() {
        return stage;
    }

    public void setStage(PurchaseOrderStage stage) {
        this.stage = stage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public List<MedicineItem> getMedicineItems() {
        return medicineItems;
    }

    public void setMedicineItems(List<MedicineItem> medicineItems) {
        this.medicineItems = medicineItems;
    }
}
