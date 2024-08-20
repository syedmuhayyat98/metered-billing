package sam.example.metered_billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sam.example.metered_billing.entity.ApiKey;
import sam.example.metered_billing.entity.Customer;
import sam.example.metered_billing.repository.ApiKeyRepository;
import sam.example.metered_billing.repository.CustomerRepository;
import sam.example.metered_billing.util.ApiKeyUtil;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Transactional
    public void addCustomerWithApiKey(String stripeCustomerId, String apiKey, boolean active, String itemId) {
        Customer customer = new Customer();
        customer.setStripeCustomerId(stripeCustomerId);
        customer.setActive(active);
        customer.setItemId(itemId);

        ApiKey key = new ApiKey();
        key.setApiKey(apiKey);
        key.setCustomer(customer);

        customer.getApiKeys().add(key);

        customerRepository.save(customer);
    }

    public Customer getCustomerByApiKey(String apiKey) {
        ApiKey key = apiKeyRepository.findByApiKey(apiKey);
        return key != null ? key.getCustomer() : null;
    }

    @Transactional
    public String generateAndSaveUniqueApiKey(Customer customer) {
        String apiKey;
        String hashedApiKey;

        do {
            apiKey = ApiKeyUtil.generateAPIKey();
            hashedApiKey = ApiKeyUtil.hashAPIKey(apiKey);
        } while (apiKeyRepository.existsById(hashedApiKey));

        ApiKey key = new ApiKey();
        key.setApiKey(hashedApiKey);
        key.setCustomer(customer);

        customer.getApiKeys().add(key);

        customerRepository.save(customer);

        return apiKey; // Return the plaintext API key to the caller
    }
}
