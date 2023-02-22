package cd.go.authorization.google.utils;

import cd.go.authorization.google.requests.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;

import cd.go.authorization.google.GoogleApiClient;
import cd.go.authorization.google.executors.RequestExecutor;
import cd.go.authorization.google.models.AuthConfig;
import cd.go.authorization.google.models.GoogleConfiguration;

import static cd.go.authorization.google.utils.Util.GSON;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.auth.oauth2.TokenVerifier;

import java.util.List;

public class GoogleIAP {
  private static String JWT_HEADER_KEY = "X-Goog-IAP-JWT-Assertion";
  private static final String IAP_ISSUER_URL = "https://cloud.google.com/iap";

  public static boolean isValidIAPRequest(final Request request, final List<AuthConfig> authConfigs) {
    String jwt = request.requestHeaders().get(JWT_HEADER_KEY);
    if (jwt == null) {
      return false;
    }

    GoogleConfiguration config = authConfigs.get(0).getConfiguration();
    return verifyJwt(
      jwt,
      config.audience());
  }

  private static boolean verifyJwt(String jwtToken, String expectedAudience) {
    System.out.println("Validating JWT ==> " + jwtToken);
    TokenVerifier tokenVerifier =
        TokenVerifier.newBuilder().setAudience(expectedAudience).setIssuer(IAP_ISSUER_URL).build();
    try {
      JsonWebToken jsonWebToken = tokenVerifier.verify(jwtToken);
      System.out.println("Token verified");

      // Verify that the token contain subject and email claims
      JsonWebToken.Payload payload = jsonWebToken.getPayload();
      System.out.println("Is valid payload?" + (payload.getSubject() != null && payload.get("email") != null));
    } catch (TokenVerifier.VerificationException e) {
      System.out.println("JWT Verification failed: " + e.getMessage());
    }
    return true;
  }
}
