/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.authorization.google.executors;

import cd.go.authorization.google.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.google.jwt.JWTValidation;
import cd.go.authorization.google.jwt.JWTValidator;
import cd.go.authorization.google.models.AuthConfig;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.IAPJwt;
import cd.go.authorization.google.requests.UserAuthenticationRequest;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserAuthenticationRequestExecutorTest {
    private static String JWT_AUDIENCE = "project/123/test";
    @Mock
    private UserAuthenticationRequest request;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private GoogleConfiguration googleConfiguration;
    @Mock
    private JWTValidator validator;

    private UserAuthenticationRequestExecutor executor;

    @BeforeEach
    public void setUp() throws Exception {
        openMocks(this);

        when(googleConfiguration.audience()).thenReturn(JWT_AUDIENCE);
        when(authConfig.getConfiguration()).thenReturn(googleConfiguration);
        executor = new UserAuthenticationRequestExecutor(request, validator);
    }

    @Test
    public void shouldErrorOutIfAuthConfigIsNotProvided() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.emptyList());

        Throwable t = assertThrows(NoAuthorizationConfigurationException.class, () -> executor.execute());
        assertThat(t.getMessage(), is("[Authenticate] No authorization configuration found."));
    }

    @Test
    public void shouldNotAuthenticate() throws Exception {
        final IAPJwt creds = new IAPJwt("a.bad.jwt");

        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.credentials()).thenReturn(creds);
        when(validator.validate("a.bad.jwt", JWT_AUDIENCE)).thenReturn(new JWTValidation(false, "foo@bar.com"));

        final GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(400));
        JSONAssert.assertEquals("Invalid IAP request", response.responseBody(), true);
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        final IAPJwt creds = new IAPJwt("a.good.jwt");

        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.credentials()).thenReturn(creds);
        when(validator.validate("a.good.jwt", JWT_AUDIENCE)).thenReturn(new JWTValidation(true, "foo@bar.com"));

        final GoPluginApiResponse response = executor.execute();

        String expectedJSON = "{\n" +
                "  \"roles\": [],\n" +
                "  \"user\": {\n" +
                "    \"username\": \"foo@bar.com\",\n" +
                "    \"display_name\": \"\",\n" +
                "    \"email\": \"foo@bar.com\"\n" +
                "  }\n" +
                "}";

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
