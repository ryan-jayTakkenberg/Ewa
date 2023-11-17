package app.jwt;

public class JWTConfig {

    private static final JWTConfig INSTANCE = new JWTConfig();

    public static JWTConfig getInstance() {
        return INSTANCE;
    }

    private String passPhrase = "7208f9bf988221644d40ce501a3afc274ce878254828f5eab38549891c28cae5";
    private String issuer = "SolarSerum";
    private int expiration = 1800;

    public String getPassphrase() {
        return passPhrase;
    }

    public String getIssuer() {
        return issuer;
    }

    public int getExpiration() {
        return expiration;
    }
}
