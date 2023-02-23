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

package cd.go.authorization.google.models;

import cd.go.authorization.google.annotation.ProfileField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import static cd.go.authorization.google.utils.Util.GSON;

public class GoogleConfiguration {
    @Expose
    @SerializedName("Audience")
    @ProfileField(key = "Audience", required = true, secure = true)
    private String audience;

    public GoogleConfiguration() {
    }

    public GoogleConfiguration(String audience) {
        this.audience = audience;
    }

    public String audience() {
        return audience;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public static GoogleConfiguration fromJSON(String json) {
        return GSON.fromJson(json, GoogleConfiguration.class);
    }

    public Map<String, String> toProperties() {
        return GSON.fromJson(toJSON(), new TypeToken<Map<String, String>>() {
        }.getType());
    }
}
