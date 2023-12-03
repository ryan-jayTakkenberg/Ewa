package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/sendTestEmail")
    public String sendTestEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("solarsedumtest@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error in sending email: " + e.getMessage();
        }
    }
}
