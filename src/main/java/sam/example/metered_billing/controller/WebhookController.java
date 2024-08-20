package sam.example.metered_billing.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sam.example.metered_billing.entity.ApiKey;
import sam.example.metered_billing.entity.Customer;
import sam.example.metered_billing.repository.ApiKeyRepository;
import sam.example.metered_billing.repository.CustomerRepository;
import sam.example.metered_billing.service.EmailService;
import sam.example.metered_billing.util.ApiKeyUtil;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private EmailService emailService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            HttpServletRequest request,
            @RequestHeader(value = "Stripe-Signature") String signature) throws IOException {

        System.out.println("Webhook endpoint triggered");
        String rawBody = request.getReader().lines().collect(Collectors.joining("\n"));

        if (rawBody.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty request body");
        }

        try {
            System.out.println("Trying to get the event type.");
            // Verify the event using the webhook secret and the raw body
            Event event = Webhook.constructEvent(rawBody, signature, webhookSecret);
            String eventType = event.getType();

            System.out.println(eventType);
            // Handle the event based on its type
            switch (eventType) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event);
                    System.out.println("Completed!");
                    break;
                case "invoice.paid":
                    // Handle invoice paid event
                    System.out.println("Paid!");
                    break;
                case "invoice.payment_failed":
                    // Handle invoice payment failed event
                    System.out.println("Payment Failed!");
                    break;
                default:
                    // Unhandled event type
                    System.out.println("Unhandled event type: " + event.getType());
                    break;
            }

            return ResponseEntity.ok("Webhook received successfully");
        } catch (SignatureVerificationException e) {
            System.err.println("Signature verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

        if (session != null) {
            String customerId = session.getCustomer();
            String subscriptionId = session.getSubscription();

            System.out.println(String.format("💰 Customer %s subscribed to plan %s", customerId, subscriptionId));

            try {
                Subscription subscription = Subscription.retrieve(subscriptionId);
                String itemId = subscription.getItems().getData().get(0).getId();

                String apiKey = ApiKeyUtil.generateAPIKey();
                String hashedAPIKey = ApiKeyUtil.hashAPIKey(apiKey);

                System.out.println("User's API Key: " + apiKey);

                // Retrieve or create a customer
                Customer customer = customerRepository.findById(customerId).orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setStripeCustomerId(customerId);
                    newCustomer.setActive(true);
                    newCustomer.setItemId(itemId);
                    return newCustomer;
                });

                // Create and associate the API key with the customer
                ApiKey newApiKey = new ApiKey();
                newApiKey.setApiKey(hashedAPIKey);
                newApiKey.setCustomer(customer);

//                Set<ApiKey> apiKeys = customer.getApiKeys();
//                if (apiKeys == null) {
//                    apiKeys = new HashSet<>();
//                    customer.setApiKeys(apiKeys);
//                }
//                apiKeys.add(newApiKey);

                // Save the customer and API key
                customerRepository.save(customer);
                apiKeyRepository.save(newApiKey);

                emailService.sendCreateReservationEmail(apiKey, session.getCustomerDetails().getEmail());

            } catch (Exception e) {
                System.out.println("Failed to handle checkout session completed: " + e.getMessage());
            }
        }
    }
}
