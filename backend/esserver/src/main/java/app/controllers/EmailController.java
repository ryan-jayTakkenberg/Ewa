package app.controllers;

import app.Util;
import app.models.User;
import app.repositories.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token) {
        User user = userJPARepository.findByResetToken(token);

        if (user != null && user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
            user.setPassword(Util.hash("itWorksoMg")); // Update password
            user.setResetToken(null); // Invalidate the reset token
            user.setResetTokenExpiry(null);
            userJPARepository.save(user);

            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }
    private void sendResetEmail(String email, String token) {
        String resetLink = "http://yourfrontenddomain.com/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("solarsedumtest@gmail.com");
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        mailSender.send(message);
    }
}
