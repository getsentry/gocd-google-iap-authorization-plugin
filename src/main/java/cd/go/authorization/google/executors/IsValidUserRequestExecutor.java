package cd.go.authorization.google.executors;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import cd.go.authorization.google.requests.IsValidUserRequest;

public class IsValidUserRequestExecutor implements RequestExecutor {
  private final IsValidUserRequest request;

  public IsValidUserRequestExecutor(IsValidUserRequest request) {
    this.request = request;
  }

  @Override
  public GoPluginApiResponse execute() throws Exception {
    // TODO: When/if GoCD supports passing in the request headers
    // for this request, we should validate the IAP header.
    /*
    JWTValidation validation = validator.validate(
      jwt,
      this.request.authConfigs().get(0).getConfiguration().audience()
    );
    if (!validation.isValid()) {
      return DefaultGoPluginApiResponse.badRequest("Invalid IAP request");
    }
    */

    return DefaultGoPluginApiResponse.success("");
  }
}
