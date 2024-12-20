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

package org.apache.gravitino.abs.credential;

import java.util.Map;
import org.apache.gravitino.credential.ADLSAccountKeyCredential;
import org.apache.gravitino.credential.Credential;
import org.apache.gravitino.credential.CredentialConstants;
import org.apache.gravitino.credential.CredentialContext;
import org.apache.gravitino.credential.CredentialProvider;
import org.apache.gravitino.credential.config.ADLSCredentialConfig;

/** Generates ADLS account key to access ADLS data. */
public class ADLSAccountKeyProvider implements CredentialProvider {
  private String accountName;
  private String accountKey;

  @Override
  public void initialize(Map<String, String> properties) {
    ADLSCredentialConfig adlsCredentialConfig = new ADLSCredentialConfig(properties);
    this.accountName = adlsCredentialConfig.storageAccountName();
    this.accountKey = adlsCredentialConfig.storageAccountKey();
  }

  @Override
  public void close() {}

  @Override
  public String credentialType() {
    return CredentialConstants.ADLS_ACCOUNT_KEY_CREDENTIAL_PROVIDER_TYPE;
  }

  @Override
  public Credential getCredential(CredentialContext context) {
    return new ADLSAccountKeyCredential(accountName, accountKey);
  }
}
