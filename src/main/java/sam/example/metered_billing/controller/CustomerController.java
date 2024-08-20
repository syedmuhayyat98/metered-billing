package sam.example.metered_billing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sam.example.metered_billing.entity.Customer;
import sam.example.metered_billing.service.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public void addCustomer(@RequestParam String stripeCustomerId,
                            @RequestParam String apiKey,
                            @RequestParam boolean active,
                            @RequestParam String itemId) {
        customerService.addCustomerWithApiKey(stripeCustomerId, apiKey, active, itemId);
    }

    @GetMapping("/findByApiKey")
    public Customer getCustomerByApiKey(@RequestParam String apiKey) {
        return customerService.getCustomerByApiKey(apiKey);
    }
}
