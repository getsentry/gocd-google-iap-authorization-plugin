package cd.go.authorization.google.requests;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import cd.go.authorization.google.executors.IsValidUserRequestExecutor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class IsValidUserRequestTest {
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
    public void shouldParseRequest() {
      IsValidUserRequest request = (IsValidUserRequest) IsValidUserRequest.from(apiRequest);

        assertThat(request.getUsername(), is("example-user@example.com"));
        assertThat("Executor is correct type", request.executor() instanceof IsValidUserRequestExecutor);
    }
}
