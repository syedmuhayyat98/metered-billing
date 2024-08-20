package sam.example.metered_billing.controller;

import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sam.example.metered_billing.exception.UnauthorizedException;
import sam.example.metered_billing.service.ApiService;

@AllArgsConstructor
@RestController
@RequestMapping("api")
public class ApiController {

    private final ApiService apiService;

    @GetMapping
    public ResponseEntity<Object> getApiData(@RequestParam(required = false) String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("API key is required");
        }

        try {
            String response = apiService.processApiRequest(apiKey);
            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Stripe error occurred");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
