package sam.example.metered_billing.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sam.example.metered_billing.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
