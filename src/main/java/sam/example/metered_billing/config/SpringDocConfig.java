package sam.example.metered_billing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "METERED-BILLING",
                version = "1.0",
                description = "API documentation for metered-billing",
                contact = @Contact(url = "https://my.sam.com/en/", name = "Syed Azril", email = "syedmuhayyat98@gmail.com")
        )
)
public class SpringDocConfig {

}
