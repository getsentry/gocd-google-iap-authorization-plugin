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
import cd.go.authorization.google.models.GoogleConfiguration;
import cd.go.authorization.google.models.TokenInfo;
import cd.go.authorization.google.requests.FetchAccessTokenRequest;
import cd.go.authorization.google.utils.GoogleIAP;

import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class FetchAccessTokenRequestExecutor implements RequestExecutor {
    private final FetchAccessTokenRequest request;

    public FetchAccessTokenRequestExecutor(FetchAccessTokenRequest request) {
        this.request = request;
    }

    public GoPluginApiResponse execute() throws Exception {
        if (request.authConfigs().isEmpty()) {
            throw new NoAuthorizationConfigurationException("[Get Access Token] No authorization configuration found.");
        }

        if (!GoogleIAP.isValidIAPRequest(this.request, this.request.authConfigs())) {
            return DefaultGoPluginApiResponse.badRequest("Invalid IAP request");
        }

        final GoogleConfiguration configuration = request.authConfigs().get(0).getConfiguration();
        final TokenInfo tokenInfo = configuration.googleApiClient().fetchAccessToken(request.requestParameters());
        return DefaultGoPluginApiResponse.success(tokenInfo.toJSON());
    }
}
