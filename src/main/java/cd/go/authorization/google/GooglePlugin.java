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

package cd.go.authorization.google;

import cd.go.authorization.google.exceptions.NoSuchRequestHandlerException;
import cd.go.authorization.google.executors.*;
import cd.go.authorization.google.requests.*;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static cd.go.authorization.google.Constants.PLUGIN_IDENTIFIER;

import java.util.Map;

@Extension
public class GooglePlugin implements GoPlugin {
    private static String PREFIX = "[IAP Auth] ";
    public static final Logger LOG = Logger.getLoggerFor(GooglePlugin.class);

    private GoApplicationAccessor accessor;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) {
        try {
            System.out.println(PREFIX + "Request Name: " + request.requestName());
            String jwtHeader = request.requestHeaders().get("X-Goog-IAP-JWT-Assertion");
            System.out.println(PREFIX + "JWT Header: " + jwtHeader);
            String headers = this.mapToString(request.requestHeaders());
            String params = this.mapToString(request.requestParameters());
            System.out.println(PREFIX + "Request Headers: " + headers);
            System.out.println(PREFIX + "Request Params: " + params);
            System.out.println(PREFIX + "Request Body: " + request.requestBody());

            switch (RequestFromServer.fromString(request.requestName())) {
                case REQUEST_GET_PLUGIN_ICON:
                    return new GetPluginIconRequestExecutor().execute();
                case REQUEST_GET_CAPABILITIES:
                    return new GetCapabilitiesRequestExecutor().execute();
                case REQUEST_GET_AUTH_CONFIG_METADATA:
                    return new GetAuthConfigMetadataRequestExecutor().execute();
                case REQUEST_AUTH_CONFIG_VIEW:
                    return new GetAuthConfigViewRequestExecutor().execute();
                case REQUEST_VALIDATE_AUTH_CONFIG:
                    return AuthConfigValidateRequest.from(request).execute();
                case REQUEST_VERIFY_CONNECTION:
                    return VerifyConnectionRequest.from(request).execute();
                case REQUEST_AUTHORIZATION_SERVER_REDIRECT_URL:
                    return GetAuthorizationServerUrlRequest.from(request).execute();
                case REQUEST_ACCESS_TOKEN:
                    return FetchAccessTokenRequest.from(request).execute();
                case REQUEST_AUTHENTICATE_USER:
                    return UserAuthenticationRequest.from(request).execute();
                case REQUEST_IS_VALID_USER:
                    return IsValidUserRequest.from(request).execute();
                default:
                    LOG.warn("Unhandled request: " + request.requestName());
                    throw new UnhandledRequestTypeException(request.requestName());
            }
        } catch (NoSuchRequestHandlerException e) {
            LOG.warn(e.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error("Error while executing request " + request.requestName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return PLUGIN_IDENTIFIER;
    }

    public String mapToString(Map<String, String> values) {
        StringBuilder mapAsString = new StringBuilder("{");
        for (String key : values.keySet()) {
            mapAsString.append(key + "=" + values.get(key) + ", ");
        }
        mapAsString.append("}");
        return mapAsString.toString();
    }
}
