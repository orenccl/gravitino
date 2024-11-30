/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.gravitino.storage;

// Defines the unified ADLS properties for different catalogs and connectors.
public class ADLSProperties {
  // The storage account name used to access ADLS.
  public static final String GRAVITINO_ADLS_ACCOUNT_NAME = "adls-account-name";
  // The static account key used to access ADLS.
  public static final String GRAVITINO_ADLS_ACCOUNT_KEY = "adls-account-key";

  // The ADLS credentials provider class name.
  public static final String GRAVITINO_ADLS_CREDS_PROVIDER = "adls-creds-provider";

  private ADLSProperties() {}
}
