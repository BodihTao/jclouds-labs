/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.azurecompute.binders;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;
import static org.jclouds.azurecompute.domain.Image.OSType.LINUX;

import javax.inject.Singleton;

import org.jclouds.azurecompute.domain.DeploymentParams;
import org.jclouds.azurecompute.domain.Image.OSType;
import org.jclouds.azurecompute.domain.InputEndpoint;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.Binder;

import com.google.common.base.Throwables;
import com.jamesmurty.utils.XMLBuilder;

@Singleton
public class BindDeploymentParamsToXmlPayload implements Binder {

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      DeploymentParams params = DeploymentParams.class.cast(input);
      try {
         XMLBuilder builder = XMLBuilder.create("Deployment").a("xmlns", "http://schemas.microsoft.com/windowsazure")
            .e("Name").t(params.getName()).up()
            .e("DeploymentSlot").t("Production").up()
            .e("Label").t(params.getName()).up()
            .e("RoleList")
            .e("Role")
            .e("RoleName").t(params.getName()).up()
            .e("RoleType").t("PersistentVMRole").up()
            .e("ConfigurationSets");

         if (params.getOsType() == OSType.WINDOWS) {
            XMLBuilder configBuilder = builder.e("ConfigurationSet"); // Windows
            configBuilder.e("ConfigurationSetType").t("WindowsProvisioningConfiguration").up()
               .e("ComputerName").t(params.getUsername()).up()
               .e("AdminPassword").t(params.getPassword()).up()
               .e("ResetPasswordOnFirstLogon").t("false").up()
               .e("EnableAutomaticUpdate").t("false").up()
               .e("DomainJoin")
               .e("Credentials")
                  .e("Domain").t(params.getName()).up()
                  .e("Username").t(params.getUsername()).up()
                  .e("Password").t(params.getPassword()).up()
               .up() // Credentials
               .e("JoinDomain").t(params.getName()).up()
               .up() // Domain Join
               .e("StoredCertificateSettings").up()
               .up(); // Windows ConfigurationSet
         } else if (params.getOsType() == OSType.LINUX) {
            XMLBuilder configBuilder = builder.e("ConfigurationSet"); // Linux
            configBuilder.e("ConfigurationSetType").t("LinuxProvisioningConfiguration").up()
               .e("HostName").t(params.getName()).up()
               .e("UserName").t(params.getUsername()).up()
               .e("UserPassword").t(params.getPassword()).up()
               .e("DisableSshPasswordAuthentication").t("false").up()
               .e("SSH").up()
               .up(); // Linux ConfigurationSet
         }

         XMLBuilder configBuilder = builder.e("ConfigurationSet"); // Network
         configBuilder.e("ConfigurationSetType").t("NetworkConfiguration").up();

         XMLBuilder inputEndpoints = configBuilder.e("InputEndpoints");
         for (InputEndpoint endpoint : params.getEndpoints()) {
            XMLBuilder inputBuilder = inputEndpoints.e("InputEndpoint");
            inputBuilder.e("LocalPort").t(endpoint.getLocalPort().toString()).up()
               .e("Name").t(endpoint.getName()).up()
               .e("Port").t(endpoint.getExternalPort().toString()).up()
               .e("Protocol").t(endpoint.getProtocol().name().toLowerCase()).up()
               .up(); //InputEndpoint
         }

         inputEndpoints.up();
         configBuilder.e("SubnetNames").up()
            .up();

         builder.up() //ConfigurationSets
            .e("DataVirtualHardDisks").up()
            .e("OSVirtualHardDisk")
            .e("HostCaching").t("ReadWrite").up()
            .e("MediaLink").t(url(params)).up()
            .e("SourceImageName").t(params.getSourceImageName()).up()
            .e("OS").t(params.getOsType() == LINUX ? "Linux" : "Windows").up()
            .up() //OSVirtualHardDisk
            .e("RoleSize").t(UPPER_UNDERSCORE.to(UPPER_CAMEL, params.getSize().name())).up()
            .up() //Role
            .up(); //RoleList

         return (R) request.toBuilder().payload(builder.asString()).build();
      } catch (Exception e) {
         throw Throwables.propagate(e);
      }
   }

   private static String url(DeploymentParams params) {
      return String.format("http://%s.blob.core.windows.net/disks/%s/%s", params.getStorageAccount(), params.getName(),
            params.getSourceImageName());
   }
}