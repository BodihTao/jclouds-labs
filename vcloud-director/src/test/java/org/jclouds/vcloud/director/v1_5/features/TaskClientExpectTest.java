/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 *(Link.builder().regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless(Link.builder().required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.features;

import static org.testng.Assert.*;

import java.net.URI;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorClient;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.Reference;
import org.jclouds.vcloud.director.v1_5.domain.Task;
import org.jclouds.vcloud.director.v1_5.domain.TasksList;
import org.jclouds.vcloud.director.v1_5.internal.BaseVCloudDirectorRestClientExpectTest;
import org.testng.annotations.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;

/**
 * Test the {@link TaskClient} by observing its side effects.
 * 
 * @author grkvlt@apache.org
 */
@Test(groups = "unit", singleThreaded = true, testName = "TaskClientExpectTest")
public class TaskClientExpectTest extends BaseVCloudDirectorRestClientExpectTest {

   @Test
   public void testTaskListForValidOrg() {
      HttpRequest orgListRequest = HttpRequest.builder()
              .method("GET")
              .endpoint(URI.create("http://localhost/api/tasksList/6f312e42-cd2b-488d-a2bb-97519cd57ed0"))
              .headers(ImmutableMultimap.<String, String> builder()
                                .put("Accept", "*/*")
                                .put("x-vcloud-authorization", token)
                                .build())
              .build();

      HttpResponse orgListResponse = HttpResponse.builder()
              .statusCode(200)
              .payload(payloadFromResourceWithContentType("/taskslist.xml", VCloudDirectorMediaType.TASKSLIST_XML + ";version=1.5"))
              .build();

      VCloudDirectorClient client = requestsSendResponses(loginRequest, sessionResponse, orgListRequest, orgListResponse);

      String orgId =Iterables.getLast(Splitter.on("/").split(URI.create("https://vcloudbeta.bluelock.com/api/org/6f312e42-cd2b-488d-a2bb-97519cd57ed0").getPath()));
      
      assertEquals(client.getTaskClient().getTaskList(orgId), TasksList.builder()
              .name("Tasks Lists")
              .type("application/vnd.vmware.vcloud.tasksList+xml")
              .href(URI.create("https://vcloudbeta.bluelock.com/api/tasksList/6f312e42-cd2b-488d-a2bb-97519cd57ed0"))
              .task(Task.builder()
                    .type("application/vnd.vmware.vcloud.task+xml")
                    .name("task")
                    .id("urn:vcloud:task:5fcd2af3-d0ec-45ce-9451-8c585a2c766b")
                    .href(URI.create("https://vcloudbeta.bluelock.com/api/task/5fcd2af3-d0ec-45ce-9451-8c585a2c766b"))
                    .status("success")
                    .operation("Created Catalog QunyingTestCatalog(7212e451-76e1-4631-b2de-ba1dfd8080e4)")
                    .operationName("catalogCreateCatalog")
                    .startTime(dateService.iso8601DateParse("2012-02-07T00:16:28.450-05:00"))
                    .endTime(dateService.iso8601DateParse("2012-02-07T00:16:28.867-05:00"))
                    .expiryTime(dateService.iso8601DateParse("2012-05-07T00:16:28.450-04:00"))
                    .owner(Reference.builder()
                          .type("application/vnd.vmware.vcloud.catalog+xml")
                          .name("QunyingTestCatalog")
                          .href(URI.create("https://vcloudbeta.bluelock.com/api/catalog/7212e451-76e1-4631-b2de-ba1dfd8080e4"))
                          .build())
                    .user(Reference.builder()
                          .type("application/vnd.vmware.admin.user+xml")
                          .name("JClouds")
                          .href(URI.create("https://vcloudbeta.bluelock.com/api/org/6f312e42-cd2b-488d-a2bb-97519cd57ed0"))
                          .build())
                    .org(Reference.builder()
                          .type("application/vnd.vmware.vcloud.org+xml")
                          .name("JClouds")
                          .href(URI.create("https://vcloudbeta.bluelock.com/api/org/6f312e42-cd2b-488d-a2bb-97519cd57ed0"))
                          .build())
                    .build())
                    .task(Task.builder()
                          .type("application/vnd.vmware.vcloud.task+xml")
                          .name("task")
                          .id("urn:vcloud:task:bd22e745-9c2a-4f82-a954-0e35b6f76ba5")
                          .href(URI.create("https://vcloudbeta.bluelock.com/api/task/bd22e745-9c2a-4f82-a954-0e35b6f76ba5"))
                          .status("success")
                          .operation("Enabled User (967d317c-4273-4a95-b8a4-bf63b78e9c69)")
                          .operationName("jobEnable")
                          .startTime(dateService.iso8601DateParse("2012-02-06T17:30:38.507-05:00"))
                          .endTime(dateService.iso8601DateParse("2012-02-06T17:30:38.507-05:00"))
                          .expiryTime(dateService.iso8601DateParse("2012-05-06T17:30:38.507-04:00"))
                          .user(Reference.builder()
                                .type("application/vnd.vmware.admin.user+xml")
                                .name("adrian@jclouds.org")
                                .href(URI.create("https://vcloudbeta.bluelock.com/api/admin/user/8c360b93-ed25-4c9a-8e24-d48cd9966d93"))
                                .build())
                          .org(Reference.builder()
                                .type("application/vnd.vmware.vcloud.org+xml")
                                .name("JClouds")
                                .href(URI.create("https://vcloudbeta.bluelock.com/api/org/6f312e42-cd2b-488d-a2bb-97519cd57ed0"))
                                .build())
                          .build())
              .build()
      );
   }
}
