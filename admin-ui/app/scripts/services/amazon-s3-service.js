/*
* Licensed to Laurent Broudoux (the "Author") under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. Author licenses this
* file to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/
'use strict';

var services = angular.module('sluiceApp.services', []).
  value('version', '0.1');

services.factory('AmazonS3Service', function($http, $q) {
  var amazonService = {
    list: function() {
      var delay = $q.defer();
      $http.get('http://localhost:9200/_river/_search?q=type:amazon-s3')
      .success(function(data, status, headers, config) {
        delay.resolve(data.hits.hits.map(function(item) {
          console.log("Item: " + JSON.stringify(item));
          var river = item._source["amazon-s3"];
          river.id = item._type;
          console.log("River: " + JSON.stringify(river));
          return river;
        }));
      });
      return delay.promise;
    },
    get: function(id) {
      var delay = $q.defer();
      $http.get('http://localhost:9200/_river/' + id + '/_meta')
      .success(function(data, status, headers, config) {
        delay.resolve(data.source);
      });
      return delay.promise;
    },
    create: function(river) {
      var delay = $q.defer();
      var data = buildData(river);
      $http.post('http://localhost:9200/_river/' + river.id + '/_meta', data);
      return delay.promise;
    },
    update: function(river) {
      var delay = $q.defer();
      var data = buildData(river);
      $http.put('http://localhost:9200/_river/' + river.id + '/_meta', data);
      return delay.promise;
    },
    delete: function(id) {
      var delay = $q.defer();
      $http.delete('http://localhost:9200/_river/' + id + '/_meta')
      return delay.promise;
    }
  }
  
  function buildData(river){
    return {
      type: 'amazon-s3',
      'amazon-s3': {
        name: river.name,
        accessKey: river.accessKey,
        secretKey: river.secretKey,
        bucket: river.bucket,
        pathPrefix: river.pathPrefix,
        update_rate: parseInt(river.update_rate),
        includes: river.includes,
        excludes: river.excludes
      }
    };
  }
  
  return amazonService;
});