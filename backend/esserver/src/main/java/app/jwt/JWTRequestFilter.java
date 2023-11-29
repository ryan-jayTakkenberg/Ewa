package app.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTConfig jwtConfig = JWTConfig.getInstance();

    private final Set<String> NO_TOKEN_ENDPOINTS = Set.of("/api/authentication/login", "/api/h2-console");

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Exclude the login endpoint from JWT validation
        String uri = request.getRequestURI().toLowerCase();
        if (NO_TOKEN_ENDPOINTS.stream().map(String::toLowerCase).anyMatch(uri::startsWith) || "OPTIONS".equals(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // get the encrypted token string from the authorization request header
        String encryptedToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        // block the request if no token was found
        if (encryptedToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
            return;
        }

        try {
            // decode the encoded and signed token, after removing optional Bearer prefix
            JWToken jwToken = JWToken.decode(encryptedToken.replace("Bearer ", ""), jwtConfig.getIssuer(), jwtConfig.getPassphrase());
            // pass-on the token info as an attribute for the request
            request.setAttribute(JWToken.JWT_ATTRIBUTE_NAME, jwToken);
            // proceed along the chain of filters towards the REST controller

            if (jwToken.getMillisecondsUntilExpires() < 5 * 60 * 1000L) {
                response.addHeader(HttpHeaders.AUTHORIZATION, jwToken.refresh(jwtConfig.getIssuer(), jwtConfig.getPassphrase(), jwtConfig.getExpiration()));
            }

            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage() + " You need to log in first.");
        }
    }
}