package sam.example.metered_billing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ApiKey {

    @Id
    private String apiKey;

    @ManyToOne
    @JoinColumn(name = "stripeCustomerId")
    private Customer customer;
}
