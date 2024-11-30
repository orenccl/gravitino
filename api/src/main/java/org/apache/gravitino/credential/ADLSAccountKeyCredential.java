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
package org.apache.gravitino.credential;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

/** ADLS Account Key credential. */
public class ADLSAccountKeyCredential implements Credential {

  /** ADLS account key credential type. */
  public static final String ADLS_ACCOUNT_KEY_CREDENTIAL_TYPE = "adls-account-key";
  /** The storage account name used to access ADLS. */
  public static final String GRAVITINO_ADLS_ACCOUNT_NAME = "adls.account.name";
  /** The static account key used to access ADLS. */
  public static final String GRAVITINO_ADLS_ACCOUNT_KEY = "adls.account.key";

  private final String accountName;
  private final String accountKey;

  /**
   * Constructs an instance of {@link ADLSAccountKeyCredential} with the static ADLS account name
   * and key.
   *
   * @param accountName The ADLS storage account name.
   * @param accountKey The static ADLS account key.
   */
  public ADLSAccountKeyCredential(String accountName, String accountKey) {
    Preconditions.checkNotNull(accountName, "ADLS account name should not be null");
    Preconditions.checkNotNull(accountKey, "ADLS account key should not be null");

    this.accountName = accountName;
    this.accountKey = accountKey;
  }

  @Override
  public String credentialType() {
    return ADLS_ACCOUNT_KEY_CREDENTIAL_TYPE;
  }

  @Override
  public long expireTimeInMs() {
    return 0; // Account key doesn't have an expiration time
  }

  @Override
  public Map<String, String> credentialInfo() {
    return ImmutableMap.of(
        GRAVITINO_ADLS_ACCOUNT_NAME, accountName,
        GRAVITINO_ADLS_ACCOUNT_KEY, accountKey);
  }

  /**
   * Get ADLS storage account name.
   *
   * @return The ADLS account name.
   */
  public String accountName() {
    return accountName;
  }

  /**
   * Get ADLS static account key.
   *
   * @return The ADLS account key.
   */
  public String accountKey() {
    return accountKey;
  }
}
