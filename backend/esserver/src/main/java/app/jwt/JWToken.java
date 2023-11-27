package app.jwt;

import app.enums.PermissionLevel;
import app.models.User;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JWToken {
    public static final String JWT_ATTRIBUTE_NAME = "jwt";
    private static final String JWT_ISSUER_CLAIM = "iss";
    private static final String JWT_IP_CLAIM = "ipa";

    private static final String JWT_ROLE_CLAIM = "rol";

    private final long accountId;
    private final PermissionLevel permissionLevel;
    private final String ip;
    private Date createdAt;
    private Date expiresAt;

    public JWToken(long accountId, PermissionLevel permissionLevel, String ip, int expiration) {
        this(accountId, permissionLevel, ip, new Date(), new Date(System.currentTimeMillis() + expiration * 1000L));
    }

    public JWToken(long accountId, PermissionLevel permissionLevel, String ip, Date createdAt, Date expiresAt) {
        this.accountId = accountId;
        this.permissionLevel = permissionLevel;
        this.ip = ip;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String encode(String issuer, String passphrase) {
        Key key = getKey(passphrase);
        return Jwts.builder()
                .claim(JWT_ROLE_CLAIM, this.permissionLevel.toString())
                .claim(JWT_IP_CLAIM, this.ip)
                .setId(String.valueOf(this.accountId))
                .setIssuer(issuer)
                .setIssuedAt(this.createdAt)
                .setExpiration(this.expiresAt)
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
                claims.get(JWT_IP_CLAIM).toString(),
                claims.getIssuedAt(),
                claims.getExpiration()
        );
    }

    /**
     * Checks if the account connected to this JWT is an admin
     * @return True if this account is an admin
     * @see User
     */
    public boolean isAdmin() {
        return permissionLevel == PermissionLevel.ADMIN;
    }

    /**
     * Checks if the account connected to this JWT is a viewer
     * @return True if this account is a viewer
     * @see User
     */
    public boolean isViewer() {
        return permissionLevel == PermissionLevel.VIEWER;
    }

    /**
     * Gets the permission level of the account connected to this JWT
     * @return The permission level of the account connected to this JWT
     * @see User
     */
    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * Gets the id of the user
     * @return The id of the user
     * @see User
     */
    public long getId() {
        return accountId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public long getMillisecondsUntilExpires() {
        return expiresAt.getTime() - System.currentTimeMillis();
    }

    public String refresh(String issuer, String passphrase, int expiration) {
        createdAt = new Date();
        expiresAt = new Date(System.currentTimeMillis() + expiration * 1000L);
        return encode(issuer, passphrase);
    }
}