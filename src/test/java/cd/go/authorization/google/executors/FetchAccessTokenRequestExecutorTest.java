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
import cd.go.authorization.google.jwt.IAPJWTValidator;
import cd.go.authorization.google.jwt.JWTValidation;
import cd.go.authorization.google.jwt.JWTValidator;
import cd.go.authorization.google.models.AuthConfig;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.IAPJwt;
import cd.go.authorization.google.requests.FetchAccessTokenRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class FetchAccessTokenRequestExecutorTest {
    @Mock
    private FetchAccessTokenRequest request;
    @Mock
    private JWTValidator validator;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private GoogleConfiguration googleConfiguration;
    private FetchAccessTokenRequestExecutor executor;

    @BeforeEach
    public void setUp() throws Exception {
        openMocks(this);

        when(authConfig.getConfiguration()).thenReturn(googleConfiguration);

        executor = new FetchAccessTokenRequestExecutor(request, validator);
    }

    @Test
    public void shouldErrorOutIfAuthConfigIsNotProvided() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.emptyList());

        Throwable t = assertThrows(NoAuthorizationConfigurationException.class, () -> executor.execute());
        assertThat(t.getMessage(), is("[Get Access Token] No authorization configuration found."));
    }

    @Test
    public void shouldFailWhenNoHeader() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.requestHeaders()).thenReturn(new HashMap<>());

        final GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(400));
        assertThat(response.responseBody(), is("Invalid IAP request"));
    }

    @Test
    public void shouldFailWhenIvalidJWTHeader() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        Map<String, String> headers = new HashMap<>();
        headers.put(IAPJWTValidator.JWT_HEADER_KEY, "A bad jwt");
        when(request.requestHeaders()).thenReturn(headers);
        when(validator.validate("A bad jwt", authConfig.getConfiguration().audience())).thenReturn(new JWTValidation(false, null));

        final GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(400));
        assertThat(response.responseBody(), is("Invalid IAP request"));
    }

    @Test
    public void shouldReturnJWT() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        Map<String, String> headers = new HashMap<>();
        headers.put(IAPJWTValidator.JWT_HEADER_KEY, "a.good.jwt");
        when(request.requestHeaders()).thenReturn(headers);
        when(validator.validate("a.good.jwt", authConfig.getConfiguration().audience())).thenReturn(new JWTValidation(true, "foo@bar.com"));

        final GoPluginApiResponse response = executor.execute();


        final String expectedJSON = "{\n" +
                "  \"jwt\": \"a.good.jwt\"\n" +
                "}";

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
