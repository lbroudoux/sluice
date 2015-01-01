/*
 * Licensed to Laurent Broudoux (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.lbroudoux.elasticsearch.sluice.rest;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.base.Strings;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.env.Environment;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugins.PluginManager;
import org.elasticsearch.plugins.PluginManager.OutputMode;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.RestRequest.Method;

import com.github.lbroudoux.elasticsearch.sluice.util.SluiceConfig;
import com.github.lbroudoux.elasticsearch.sluice.util.SluiceConfig.River;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
/**
 * REST Action definition for installing a new river.
 * @author laurent
 */
public class InstallRiverAction extends BaseRestHandler{

   @Inject
   public InstallRiverAction(Settings settings, Client client, RestController controller){
      super(settings, client);

      // Define REST endpoints.
      controller.registerHandler(Method.POST, "/_sluice/river/{name}", this);
      controller.registerHandler(Method.POST, "/_sluice/river/{name}/{version}", this);
   }

   @Override
   public void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception{
      if (logger.isDebugEnabled()){
         logger.debug("REST InstallRiverAction called");
      }

      // Retrieve river to install params.
      String riverName = request.param("name");
      String riverVersion = request.param("version");

      // Retrieve and check river parameters.
      River river = SluiceConfig.getRiverConfig(riverName);
      if (river == null){
         // River not in supported list.
         logger.error("River is not supported by Sluice, returning a Bad request response.");
         channel.sendResponse(new BytesRestResponse(RestStatus.BAD_REQUEST, riverName + " is not supported by Sluice"));
      }

      // Build pluginName from river params.
      String pluginName = river.getName();
      if (Strings.isNullOrEmpty(riverVersion)){
         pluginName += river.getDefaultVersion();
      } else {
         pluginName += riverVersion;
      }

      // Build everything we need to get a proper ES Plugin manager.
      Tuple<Settings, Environment> initialSettings = InternalSettingsPreparer.prepareSettings(EMPTY_SETTINGS, true);
      PluginManager.OutputMode outputMode = OutputMode.DEFAULT;
      TimeValue timeout = PluginManager.DEFAULT_TIMEOUT;
      PluginManager pluginManager = new PluginManager(initialSettings.v2(), null, outputMode, timeout);

      // Launch plugin manager installation process.
      boolean installed = false;
      try {
         logger.info("-> Installing " + pluginName + "...");
         pluginManager.downloadAndExtract(pluginName);
         logger.info("Installed " + pluginName);
         installed = true;
      } catch (IOException ioe) {
         logger.error("Failed to install " + pluginName + ", reason: " + ioe.getMessage());
      } catch (Throwable t) {
         logger.error("Error while installing plugin, reason: " + t.getClass().getSimpleName() +
               ": " + t.getMessage());
      }

      // Prepare response.
      XContentBuilder builder = jsonBuilder();
      try {
         builder
            .startObject()
               .field(new XContentBuilderString("installation_success"), installed)
            .endObject();
         channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
      } catch (IOException e) {
         onFailure(request, channel, e);
      }
   }

   /** */
   protected void onFailure(RestRequest request, RestChannel channel, Exception e) throws Exception{
      try {
         channel.sendResponse(new BytesRestResponse(channel, e));
      } catch (IOException ioe){
         logger.error("Sending failure response fails !", e);
         channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR));
      }
   }
}
