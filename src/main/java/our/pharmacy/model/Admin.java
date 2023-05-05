package our.pharmacy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends User {

    @Override
    public Role getRole() {
        return Role.ROLE_ADMIN;
    }
}
