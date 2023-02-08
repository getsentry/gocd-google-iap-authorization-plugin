package cd.go.authorization.google.requests;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import cd.go.authorization.google.executors.IsValidUserRequestExecutor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IsValidUserRequest extends Request {
    @Expose
    @SerializedName("username")
    private String username;

    public static IsValidUserRequest from(GoPluginApiRequest apiRequest) {
      return Request.from(apiRequest, IsValidUserRequest.class);
  }

    @Override
    public IsValidUserRequestExecutor executor() {
        return new IsValidUserRequestExecutor(this);
    }

    public String getUsername() {
        return username;
    }
}
