/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.joyent.cloudapi.v6_5.internal;

import java.net.URI;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.joyent.cloudapi.v6_5.JoyentCloudClient;

import com.google.common.collect.ImmutableMultimap;

/**
 * Base class for writing Cloud Api Rest Client Expect tests
 * 
 * @author Adrian Cole
 */
public class BaseJoyentCloudClientExpectTest extends BaseJoyentCloudExpectTest<JoyentCloudClient> {
   protected HttpRequest getDatacenters = HttpRequest
         .builder()
         .method("GET")
         .endpoint(URI.create("https://api.joyentcloud.com/my/datacenters"))
         .headers(
               ImmutableMultimap.<String, String> builder()
                  .put("X-Api-Version", "~6.5")
                  .put("Accept", "application/json")
                  .put("Authorization", "Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==")
                  .build()).build();
   
   protected HttpResponse getDatacentersResponse = HttpResponse.builder().statusCode(200)
         .payload(payloadFromResource("/datacenters.json")).build();
}
