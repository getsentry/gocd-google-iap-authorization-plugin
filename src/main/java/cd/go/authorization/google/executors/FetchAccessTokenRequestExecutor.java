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
import cd.go.authorization.google.models.IAPJwt;
import cd.go.authorization.google.requests.FetchAccessTokenRequest;

import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class FetchAccessTokenRequestExecutor implements RequestExecutor {
    private final FetchAccessTokenRequest request;
    private JWTValidator validator;

    public FetchAccessTokenRequestExecutor(FetchAccessTokenRequest request, JWTValidator validator) {
        this.request = request;
        this.validator = validator;
    }

    public GoPluginApiResponse execute() throws Exception {
        if (request.authConfigs().isEmpty()) {
            throw new NoAuthorizationConfigurationException("[Get Access Token] No authorization configuration found.");
        }

        String jwt = this.request.requestHeaders().get(IAPJWTValidator.JWT_HEADER_KEY);
        if (jwt == null) {
            return DefaultGoPluginApiResponse.badRequest("Invalid IAP request");
        }

        JWTValidation validation = validator.validate(
            jwt,
            this.request.authConfigs().get(0).getConfiguration().audience()
        );
        if (!validation.isValid()) {
            return DefaultGoPluginApiResponse.badRequest("Invalid IAP request");
        }

        IAPJwt payload = new IAPJwt(jwt);
        return DefaultGoPluginApiResponse.success(payload.toJSON());
    }
}
