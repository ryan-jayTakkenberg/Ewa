package app.controllers;

import app.Util;
import app.models.User;
import app.repositories.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserJPARepository userJPARepository;

    private String generateResetToken() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        User user = userJPARepository.findByEmail(email);

        if (user != null) {
            String resetToken = generateResetToken();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
            userJPARepository.save(user); // Save the updated user entity

            sendResetEmail(email, resetToken);
        } else {
            System.out.println("Email does not exist in database");
        }

        return ResponseEntity.ok().build();
    }

    // Endpoint to validate the token
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
        User user = userJPARepository.findByResetToken(token);

        if (user != null && user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
            return ResponseEntity.ok("Token is valid.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    // Endpoint to actually reset the password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        User user = userJPARepository.findByResetToken(token);

        if (user != null && user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
            user.setPassword(Util.hash(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userJPARepository.save(user);

            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }
    private void sendResetEmail(String email, String token) {
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("solarsedumtest@gmail.com");
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        mailSender.send(message);
    }

    // todo sendEmail for reports/forecasting alerts
}