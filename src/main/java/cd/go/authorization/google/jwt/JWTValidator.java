package cd.go.authorization.google.jwt;

public interface JWTValidator {
  JWTValidation validate(String jwt, String audience);
}
