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

package org.apache.gravitino.iceberg.integration.test;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.gravitino.credential.CredentialConstants;
import org.apache.gravitino.iceberg.common.IcebergConfig;
import org.apache.gravitino.integration.test.util.BaseIT;
import org.apache.gravitino.integration.test.util.DownloaderUtils;
import org.apache.gravitino.integration.test.util.ITUtils;
import org.apache.gravitino.storage.ADLSProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("FormatStringAnnotation")
// @EnabledIfEnvironmentVariable(named = "GRAVITINO_TEST_CLOUD_IT", matches = "true")
public class IcebergRESTADLSIT extends IcebergRESTJdbcCatalogIT {

  private String accountName;
  private String accountKey;

  @Override
  void initEnv() {
    this.accountName = getFromEnvOrDefault("GRAVITINO_ADLS_ACCOUNT_NAME", "{ACCESS_KEY}");
    this.accountKey = getFromEnvOrDefault("GRAVITINO_ADLS_ACCOUNT_KEY", "{ACCESS_KEY}");
    if (ITUtils.isEmbedded()) {
      return;
    }
    try {
      downloadIcebergAzureBundleJar();
    } catch (IOException e) {
      LOG.warn("Download Iceberg Azure bundle jar failed,", e);
      throw new RuntimeException(e);
    }
    copyAzureBundleJar();
  }

  @Override
  public Map<String, String> getCatalogConfig() {
    HashMap<String, String> config = new HashMap<>();
    config.putAll(getCatalogJdbcConfig());
    config.putAll(getADLSConfig());
    return config;
  }

  public boolean supportsCredentialVending() {
    return true;
  }

  private Map<String, String> getADLSConfig() {
    Map configMap = new HashMap<String, String>();

    configMap.put(
        IcebergConfig.ICEBERG_CONFIG_PREFIX + CredentialConstants.CREDENTIAL_PROVIDER_TYPE,
        CredentialConstants.ADLS_ACCOUNT_KEY_CREDENTIAL_PROVIDER_TYPE);
    configMap.put(
        IcebergConfig.ICEBERG_CONFIG_PREFIX + ADLSProperties.GRAVITINO_ADLS_ACCOUNT_NAME,
        accountName);
    configMap.put(
        IcebergConfig.ICEBERG_CONFIG_PREFIX + ADLSProperties.GRAVITINO_ADLS_ACCOUNT_KEY,
        accountKey);

    return configMap;
  }

  private void downloadIcebergAzureBundleJar() throws IOException {
    String icebergBundleJarName = "iceberg-azure-bundle-1.5.2.jar";
    String icebergBundleJarUri =
        "https://repo1.maven.org/maven2/org/apache/iceberg/"
            + "iceberg-azure-bundle/1.5.2/"
            + icebergBundleJarName;
    String gravitinoHome = System.getenv("GRAVITINO_HOME");
    String targetDir = String.format("%s/iceberg-rest-server/libs/", gravitinoHome);
    DownloaderUtils.downloadFile(icebergBundleJarUri, targetDir);
  }

  private void copyAzureBundleJar() {
    String gravitinoHome = System.getenv("GRAVITINO_HOME");
    String targetDir = String.format("%s/iceberg-rest-server/libs/", gravitinoHome);
    BaseIT.copyBundleJarsToDirectory("azure-bundle", targetDir);
  }

  @Test
  void testDML() {
    String namespaceName = ICEBERG_REST_NS_PREFIX + "dml";
    String tableName = namespaceName + ".test";
    sql("CREATE DATABASE IF NOT EXISTS " + namespaceName);
    sql(
        String.format(
            "CREATE TABLE %s (id bigint COMMENT 'unique id',data string, ts timestamp) USING iceberg "
                + "PARTITIONED BY (bucket(2, id), days(ts))",
            tableName));
    sql(
        String.format(
            " INSERT INTO %s VALUES (1, 'a', cast('2023-10-01 01:00:00' as timestamp));",
            tableName));
    sql(
        String.format(
            " INSERT INTO %s VALUES (2, 'b', cast('2023-10-02 01:00:00' as timestamp));",
            tableName));
    sql(
        String.format(
            " INSERT INTO %s VALUES (3, 'c', cast('2023-10-03 01:00:00' as timestamp));",
            tableName));
    sql(
        String.format(
            " INSERT INTO %s VALUES (4, 'd', cast('2023-10-04 01:00:00' as timestamp));",
            tableName));
    Map<String, String> m =
        convertToStringMap(sql("SELECT * FROM " + tableName + " WHERE ts > '2023-10-03 00:00:00'"));
    Assertions.assertEquals(m, ImmutableMap.of("3", "c", "4", "d"));
  }
}
