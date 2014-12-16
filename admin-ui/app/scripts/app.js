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

angular
  .module('sluiceApp', [
    'sluiceApp.services',
    'sluiceApp.directives',
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ui.bootstrap',
    'frapontillo.bootstrap-switch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/dashboard.html',
        controller: 'DashboardController',
        section: 'dashboard',
        resolve: {
          rivers: function(DashboardService) {
            return DashboardService.availableRivers();
          }
        }
      })
      .when('/amazon-s3', {
        templateUrl: 'views/amazon-s3.html',
        controller: 'AmazonS3Controller',
        section: 'amazon-s3',
        resolve: {
          rivers: function(AmazonS3Service) {
            return AmazonS3Service.list();
          }
        }
      })
      .otherwise({
        redirectTo: '/'
      });
  });