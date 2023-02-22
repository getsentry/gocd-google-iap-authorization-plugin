package cd.go.authorization.google.utils;

import cd.go.authorization.google.requests.Request;

public class GoogleIAP {
  private static String JWT_HEADER_KEY = "X-Goog-IAP-JWT-Assertion";

  public static boolean isValidIAPRequest(final Request request) {
    String jwtHeader = request.requestHeaders().get(JWT_HEADER_KEY);
    if (jwtHeader == null || jwtHeader == "") {
      return false;
    }

    System.out.println("JWT Header ==> " + jwtHeader);
    System.out.println("TODO: Validate the JWT");

    return true;
  }
}
