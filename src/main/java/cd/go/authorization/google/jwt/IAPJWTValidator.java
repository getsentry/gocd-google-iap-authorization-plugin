package cd.go.authorization.google.jwt;

import com.google.auth.oauth2.TokenVerifier;
import com.google.api.client.json.webtoken.JsonWebToken;

public class IAPJWTValidator implements JWTValidator {
  public static String JWT_HEADER_KEY = "X-Goog-IAP-JWT-Assertion";
  private static final String IAP_ISSUER_URL = "https://cloud.google.com/iap";

  public JWTValidation validate(String jwt, String audience) {
    TokenVerifier tokenVerifier =
        TokenVerifier.newBuilder().setAudience(audience).setIssuer(IAP_ISSUER_URL).build();

    try {
      JsonWebToken jsonWebToken = tokenVerifier.verify(jwt);
      JsonWebToken.Payload payload = jsonWebToken.getPayload();
      if (payload.getSubject() != null && payload.get("email") != null) {
        return new JWTValidation(true, payload.get("email").toString());
      }
    } catch (TokenVerifier.VerificationException e) {
      System.out.println("JWT Verification failed: " + e.getMessage());
    }

    return new JWTValidation(false, null);
  }
}
