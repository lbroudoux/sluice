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
package com.github.lbroudoux.elasticsearch.sluice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
/**
 * Sluice configuration holder and utilities.
 * @author laurent
 */
public class SluiceConfig {

   /** Properties holder for sluice.properties file. */
   private static Properties properties = new Properties();

   private static final String RIVER_LIST_PROP = "sluice.rivers";

   private static final String RIVER_PROPS_PREFIX = "sluice.river";

   /**
    * Get the list of supported rivers.
    * @return A list of river name.
    * @throws ConfigurationException if config cannot be retrieved.
    */
   public static List<String> getSupportedRivers() throws ConfigurationException{
      if (properties.isEmpty()){
         loadSluiceProperties();
      }
      String riverList = properties.getProperty(RIVER_LIST_PROP);
      return Arrays.asList(riverList.split(","));
   }

   public static River getRiverConfig(String river) throws ConfigurationException{
      if (!getSupportedRivers().contains(river)){
         return null;
      }
      String name = properties.getProperty(RIVER_PROPS_PREFIX + "." + river + ".name");
      String defaultVersion = properties.getProperty(RIVER_PROPS_PREFIX + "." + river + ".defaultVersion");
      return new River(name, defaultVersion);
   }

   /** Simple bean wrapper around river configuration. */
   public static class River{
      private String name;
      private String defaultVersion;

      /**
       * Build a new river with plugin name and default version.
       * @param name The plugin name of river
       * @param defaultVersion The default version of river
       */
      private River(final String name, final String defaultVersion){
         this.name = name;
         this.defaultVersion = defaultVersion;
      }
      /** @return This river name. */
      public String getName(){
         return name;
      }
      /** @return This river default version. */
      public String getDefaultVersion(){
         return defaultVersion;
      }
   }

   /** Load sluice.properties file. */
   private static void loadSluiceProperties() throws ConfigurationException{
      InputStream is  = SluiceConfig.class.getResourceAsStream("/sluice.properties");
      try {
         properties.load(is);
      } catch (IOException ioe) {
         throw new ConfigurationException("sluice.properties config file cannot be found into sluice jar");
      }
   }
}
