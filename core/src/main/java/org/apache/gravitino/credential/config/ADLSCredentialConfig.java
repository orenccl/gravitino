/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.gravitino.credential.config;

import java.util.Map;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.gravitino.Config;
import org.apache.gravitino.config.ConfigBuilder;
import org.apache.gravitino.config.ConfigConstants;
import org.apache.gravitino.config.ConfigEntry;
import org.apache.gravitino.storage.ADLSProperties;

public class ADLSCredentialConfig extends Config {

  public static final ConfigEntry<String> ADLS_ACCOUNT_NAME =
      new ConfigBuilder(ADLSProperties.GRAVITINO_ADLS_ACCOUNT_NAME)
          .doc("The name of the Azure Data Lake Storage account")
          .version(ConfigConstants.VERSION_0_7_0)
          .stringConf()
          .checkValue(StringUtils::isNotBlank, ConfigConstants.NOT_BLANK_ERROR_MSG)
          .create();

  public static final ConfigEntry<String> ADLS_ACCOUNT_KEY =
      new ConfigBuilder(ADLSProperties.GRAVITINO_ADLS_ACCOUNT_KEY)
          .doc("The access key of the Azure Data Lake Storage account")
          .version(ConfigConstants.VERSION_0_7_0)
          .stringConf()
          .checkValue(StringUtils::isNotBlank, ConfigConstants.NOT_BLANK_ERROR_MSG)
          .create();

  public ADLSCredentialConfig(Map<String, String> properties) {
    super(false);
    loadFromMap(properties, k -> true);
  }

  @NotNull
  public String accountName() {
    return this.get(ADLS_ACCOUNT_NAME);
  }

  @NotNull
  public String accountKey() {
    return this.get(ADLS_ACCOUNT_KEY);
  }
}
