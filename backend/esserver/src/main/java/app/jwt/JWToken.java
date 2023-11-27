package app.jwt;

import app.enums.PermissionLevel;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JWToken {
    public static final String JWT_ATTRIBUTE_NAME = "att";
    private static final String JWT_ISSUER_CLAIM = "iss";
    private static final String JWT_IP_CLAIM = "ipa";

    private static final String JWT_ROLE_CLAIM = "rol";

    private final long accountId;
    private final PermissionLevel permissionLevel;
    private final String ip;

    public JWToken(long accountId, PermissionLevel permissionLevel, String ip) {
        this.accountId = accountId;
        this.permissionLevel = permissionLevel;
        this.ip = ip;
    }

    public String encode(String issuer, String passphrase, int expiration) {
        Key key = getKey(passphrase);
        return Jwts.builder()
                .claim(JWT_ROLE_CLAIM, this.permissionLevel.toString())
                .claim(JWT_IP_CLAIM, this.ip)
                .setId(String.valueOf(this.accountId))
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private static Key getKey(String passphrase) {
        byte[] hmacKey = passphrase.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    }

    public static JWToken decode(String token, String issuer, String passphrase)
            throws ExpiredJwtException, MalformedJwtException {
        // Validate the token string and extract the claims
        Key key = getKey(passphrase);
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token);
        Claims claims = jws.getBody();
        // check the issuer claim
        if (!claims.get(JWT_ISSUER_CLAIM).toString().equals(issuer)) {
            throw new MalformedJwtException("Invalid issuer");
        }
        // build our token from the extracted claims
        return new JWToken(
                Long.parseLong(claims.getId()),
                PermissionLevel.valueOf(claims.get(JWT_ROLE_CLAIM).toString()),
                claims.get(JWT_IP_CLAIM).toString()
        );
    }

    /**
     * Checks if the account connected to this JWT is an admin
     * @return True if this account is an admin
     * @see app.models.UserModel
     */
    public boolean isAdmin() {
        return permissionLevel == PermissionLevel.ADMIN;
    }

    /**
     * Checks if the account connected to this JWT is a viewer
     * @return True if this account is a viewer
     * @see app.models.UserModel
     */
    public boolean isViewer() {
        return permissionLevel == PermissionLevel.VIEWER;
    }

    /**
     * Gets the permission level of the account connected to this JWT
     * @return The permission level of the account connected to this JWT
     * @see app.models.UserModel
     */
    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * Gets the id of the user
     * @return The id of the user
     * @see app.models.UserModel
     */
    public long getId() {
        return accountId;
    }
}