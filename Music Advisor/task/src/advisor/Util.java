package advisor;

public final class Util {

    private Util() {
        throw new IllegalStateException("Util class");
    }

    public static String AUTH_SERVER = "https://accounts.spotify.com";

    public static final String API_SERVER = "https://api.spotify.com";

    public static final String CLIENT_ID = "9160aebd21f244c2bf424688b92429cc";
    public static final String CLIENT_SECRET = "261efa4a21fc43989ba4460b27b06eeb";
    public static final String AUTHORIZE_PART = "/authorize";
    public static final String RESPONSE_TYPE = "code";
    public static final String TOKEN_PART = "/api/token";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String REDIRECT_URI = "http://localhost:8080";

    public static final String ANSWER_DENIED_ACCESS =
            "Please, provide access for application.";
    public static void setAuthServer(String authServer){
        AUTH_SERVER = authServer;
    }
}
