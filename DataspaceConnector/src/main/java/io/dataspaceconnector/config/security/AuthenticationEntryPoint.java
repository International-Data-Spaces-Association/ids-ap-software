/*
 * Copyright 2022 sovity GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dataspaceconnector.config.security;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Sets Settings for the BasicAuthenticationEntryPoint.
 */
@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    /**
     * Sets additional properties after default are set.
     */
    @Override
    public void afterPropertiesSet() {
        setRealmName("admin realm");
        super.afterPropertiesSet();
    }
}
