package sam.example.metered_billing.service;

import com.stripe.exception.StripeException;
import com.stripe.model.UsageRecord;
import com.stripe.param.UsageRecordCreateOnSubscriptionItemParams;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sam.example.metered_billing.entity.Customer;
import sam.example.metered_billing.exception.UnauthorizedException;
import sam.example.metered_billing.repository.ApiKeyRepository;
import sam.example.metered_billing.util.ApiKeyUtil;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ApiService {

    @Autowired
    private final ApiKeyRepository apiKeyRepository;


    public String processApiRequest(String apiKey) throws StripeException, UnauthorizedException {
        String hashedAPIKey = ApiKeyUtil.hashAPIKey(apiKey);

        Optional<Customer> customerOpt = Optional.ofNullable(apiKeyRepository.findByApiKey(hashedAPIKey).getCustomer());

        if (customerOpt.isEmpty() || !customerOpt.get().isActive()) {
            throw new UnauthorizedException();
        }

        Customer customer = customerOpt.get();
        // Record usage with Stripe
        UsageRecordCreateOnSubscriptionItemParams params = UsageRecordCreateOnSubscriptionItemParams.builder()
                .setQuantity(1L)
                .build();

        UsageRecord usageRecord = UsageRecord.createOnSubscriptionItem(customer.getItemId(), params);

        return "🔥🔥🔥🔥🔥🔥🔥🔥";
    }
}
