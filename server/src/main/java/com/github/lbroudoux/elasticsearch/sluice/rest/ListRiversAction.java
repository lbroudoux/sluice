package com.github.lbroudoux.elasticsearch.sluice.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.env.Environment;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.RestRequest.Method;

import com.github.lbroudoux.elasticsearch.sluice.util.SluiceConfig;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.EMPTY_SETTINGS;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
/**
 * REST Action definition for listing supported and already installed rivers.
 * @author laurent
 */
public class ListRiversAction extends BaseRestHandler{

   @Inject
   public ListRiversAction(Settings settings, Client client, RestController controller) {
      super(settings, client);

      // Define REST endpoint.
      controller.registerHandler(Method.GET, "/_sluice/river", this);
   }

   @Override
   public void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception{
      if (logger.isDebugEnabled()){
         logger.debug("REST ListRiversAction called");
      }

      // Retrieve the list of supported rivers.
      List<String> supportedRivers = SluiceConfig.getSupportedRivers();

      // Retrieve the list of already installed plugins.
      Tuple<Settings, Environment> initialSettings = InternalSettingsPreparer.prepareSettings(EMPTY_SETTINGS, true);
      File[] pluginFiles = initialSettings.v2().pluginsFile().listFiles();
      List<String> plugins = new ArrayList<String>();
      for (File pluginFile : pluginFiles){
         plugins.add(pluginFile.getName());
      }

      // Prepare response.
      XContentBuilder builder = jsonBuilder();
      try {
         builder.startArray();
         for (String river : supportedRivers){
            builder
               .startObject()
                  .field("name", river)
                  .field("installed", plugins.contains(river))
               .endObject();
         }
         builder.endArray();
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
