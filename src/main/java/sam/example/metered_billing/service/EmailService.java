package sam.example.metered_billing.service;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
public class EmailService {

    private final TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

//    @Value("${spring.mail.username}")
//    private String sender;

    public void sendCreateReservationEmail(String apiToken, String email) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        final Context context = new Context();
        context.setVariable("apiToken", apiToken);
        context.setVariable("email", email);
        String emailContent = templateEngine.process("token-creation-email.html", context);

        helper.setFrom("az.hayyat98@gmail.com");
        helper.setTo(email);
        helper.setText(emailContent, true);
        helper.setSubject("Api Token Email");
        mailSender.send(message);
    }
}
