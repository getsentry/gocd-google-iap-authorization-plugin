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
import cd.go.authorization.google.models.User;
import cd.go.authorization.google.requests.UserAuthenticationRequest;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final UserAuthenticationRequest request;
    private JWTValidator validator;

    public UserAuthenticationRequestExecutor(UserAuthenticationRequest request, JWTValidator validator) {
        this.request = request;
        this.validator = validator;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        if (request.authConfigs().isEmpty()) {
            throw new NoAuthorizationConfigurationException("[Authenticate] No authorization configuration found.");
        }

        JWTValidation validation = validator.validate(
            this.request.credentials().jwt(),
            this.request.authConfigs().get(0).getConfiguration().audience()
        );

        if (!validation.isValid()) {
            return DefaultGoPluginApiResponse.badRequest("Invalid IAP request");
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", new User(validation.email(), "", validation.email()));
        userMap.put("roles", Collections.emptyList());
        return DefaultGoPluginApiResponse.success(GSON.toJson(userMap));
    }
}
