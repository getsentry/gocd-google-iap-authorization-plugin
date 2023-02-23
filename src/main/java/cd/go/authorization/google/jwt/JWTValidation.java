package cd.go.authorization.google.jwt;

public class JWTValidation {
  private final Boolean isValid;
  private final String email;

  public JWTValidation(Boolean isValid, String email) {
    this.isValid = isValid;
    this.email = email;
  }

  public Boolean isValid() {
    return this.isValid;
  }

  public String email() {
    return this.email;
  }
}
