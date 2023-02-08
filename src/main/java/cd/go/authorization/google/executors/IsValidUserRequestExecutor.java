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
    // TODO: Verify the IAP JWT headers.
    // See https://cloud.google.com/iap/docs/signed-headers-howto
    return DefaultGoPluginApiResponse.success("");
  }
}
