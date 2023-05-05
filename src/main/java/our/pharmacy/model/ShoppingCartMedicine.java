package our.pharmacy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart_medicine")
public class ShoppingCartMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @Column(nullable = false)
    private int number;

    // constructors, getters and setters

}
