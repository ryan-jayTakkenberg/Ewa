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
    private static final String JWT_CALLNAME_CLAIM = "sub";
    private static final String JWT_ACCOUNTID_CLAIM = "id";
    private static final String JWT_ROLE_CLAIM = "rol";

    private String callName;
    private long accountId;
    private PermissionLevel permissionLevel;

    public JWToken(String callName, long accountId, PermissionLevel permissionLevel) {
        this.callName = callName;
        this.accountId = accountId;
        this.permissionLevel = permissionLevel;
    }

    public String encode(String issuer, String passphrase, int expiration) {
        Key key = getKey(passphrase);
        return Jwts.builder()
                .claim(JWT_CALLNAME_CLAIM, this.callName)
                .claim(JWT_ACCOUNTID_CLAIM, this.accountId)
                .claim(JWT_ROLE_CLAIM, this.permissionLevel.toString())
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
                claims.get(JWT_CALLNAME_CLAIM).toString(),
                Long.parseLong(claims.get(JWT_ACCOUNTID_CLAIM).toString()),
                PermissionLevel.valueOf(claims.get(JWT_ROLE_CLAIM).toString())
        );
    }

    public boolean isAdmin() {
        return permissionLevel == PermissionLevel.ADMIN;
    }

    /**
     * Gets the id of the user (see UserModel)
     * @return the id of the user
     */
    public long getId() {
        return accountId;
    }
}