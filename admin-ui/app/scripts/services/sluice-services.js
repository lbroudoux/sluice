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

services.factory('DashboardService', function($http, $q) {
  var dashService = {
    availableRivers: function() {
      var delay = $q.defer();
      $http.get('http://localhost:9200/_sluice/river')
      .success(function(data, status, headers, config) {
        delay.resolve(data);
      });
      return delay.promise;
    }
  };
  return dashService;
});