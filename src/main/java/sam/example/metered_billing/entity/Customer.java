package sam.example.metered_billing.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Customer {

    @Id
    private String stripeCustomerId;

    private String itemId;

    private boolean active;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<ApiKey> apiKeys;
}
