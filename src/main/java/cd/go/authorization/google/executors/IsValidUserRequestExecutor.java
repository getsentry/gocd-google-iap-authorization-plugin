package cd.go.authorization.google.executors;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import cd.go.authorization.google.requests.IsValidUserRequest;

import static cd.go.authorization.google.GooglePlugin.LOG;

import java.util.Map;

public class IsValidUserRequestExecutor implements RequestExecutor {
  private final IsValidUserRequest request;

  public IsValidUserRequestExecutor(IsValidUserRequest request) {
    this.request = request;
  }

  @Override
  public GoPluginApiResponse execute() throws Exception {
    Map<String, String> headers = this.request.requestHeaders();
    StringBuilder mapAsString = new StringBuilder("{");
    for (String key : headers.keySet()) {
        mapAsString.append(key + "=" + headers.get(key) + ", ");
    }
    mapAsString.append("}");
    System.out.println("Headers for the isValidUserRequestExecutor => " + mapAsString.toString());
    LOG.info("Headers for the isValidUserRequestExecutor => " + mapAsString.toString());
    // TODO: Verify the IAP JWT headers.
    // See https://cloud.google.com/iap/docs/signed-headers-howto
    return DefaultGoPluginApiResponse.success("");
  }
}
