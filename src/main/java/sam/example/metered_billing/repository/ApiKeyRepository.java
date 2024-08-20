package sam.example.metered_billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sam.example.metered_billing.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
    ApiKey findByApiKey(String apiKey);
}
