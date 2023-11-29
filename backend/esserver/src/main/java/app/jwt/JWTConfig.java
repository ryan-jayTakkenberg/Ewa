package app.jwt;

public class JWTConfig {

    private static final JWTConfig INSTANCE = new JWTConfig();

    public static JWTConfig getInstance() {
        return INSTANCE;
    }

    public String getPassphrase() {
        return "7208f9bf988221644d40ce501a3afc274ce878254828f5eab38549891c28cae5";
    }

    public String getIssuer() {
        return "SolarSerum";
    }

    public int getExpiration() {
        return 1800;
    }
}
