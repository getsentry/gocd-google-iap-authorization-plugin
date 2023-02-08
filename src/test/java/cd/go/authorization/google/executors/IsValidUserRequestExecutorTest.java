package cd.go.authorization.google.executors;

import cd.go.authorization.google.requests.IsValidUserRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class IsValidUserRequestExecutorTest {
  @Mock
  private GoPluginApiRequest apiRequest;

  @BeforeEach
  public void setUp() {
    openMocks(this);
    when(apiRequest.requestBody()).thenReturn("{\n" +
      "  \"auth_config\": {\n" +
      "    \"configuration\": {\n" +
      "       \"ClientId\": \"client-id.apps.googleusercontent.com\",\n" +
      "       \"ClientSecret\": \"client-secret\",\n" +
      "       \"AllowedDomains\": \"example.com\"\n" +
      "    },\n" +
      "    \"id\": \"google\"\n" +
      "  },\n" +
      "  \"username\": \"example-user@example.com\"\n" +
      "}");
  }

  @Test
  public void shouldParseRequest() throws Exception {
    IsValidUserRequest request = IsValidUserRequest.from(apiRequest);
    IsValidUserRequestExecutor executor = new IsValidUserRequestExecutor(request);

    GoPluginApiResponse response = executor.execute();

    assertThat(response.responseCode(), is(200));
    assertThat(response.responseBody(), is(""));
  }
}
