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
import cd.go.authorization.google.models.AuthConfig;
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.requests.GetAuthorizationServerUrlRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetAuthorizationServerUrlRequestExecutorTest {

    @Mock
    private GetAuthorizationServerUrlRequest request;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private GoogleConfiguration googleConfiguration;

    private GetAuthorizationServerUrlRequestExecutor executor;

    @BeforeEach
    public void setUp() throws Exception {
        openMocks(this);

        executor = new GetAuthorizationServerUrlRequestExecutor(request);
    }

    @Test
    public void shouldErrorOutIfAuthConfigIsNotProvided() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.emptyList());

        Throwable t = assertThrows(NoAuthorizationConfigurationException.class, () -> executor.execute());
        assertThat(t.getMessage(), Matchers.is("[Authorization Server Url] No authorization configuration found."));
    }

    @Test
    public void shouldReturnAuthorizationServerUrl() throws Exception {
        when(authConfig.getConfiguration()).thenReturn(googleConfiguration);
        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.callbackUrl()).thenReturn("https://gocd.example.dev/example.plugin.callback");

        final GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(200));
        assertThat(response.responseBody(), startsWith("{\"authorization_server_url\":\"https://gocd.example.dev/example.plugin.callback\"}"));
    }
}
