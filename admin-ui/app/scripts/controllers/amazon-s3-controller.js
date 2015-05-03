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

angular.module('sluiceApp')
  .controller('AmazonS3Controller', function ($rootScope, $scope, $modal, notify, rivers, AmazonS3Service) {
  
  $scope.rivers = rivers;
  
  $scope.addRiver = function(river) {
    river = {update_rate: 1000 * 60 * 10, bulk_size: 2};   // jshint ignore:line
    var modalInstance = show(river, 'edit-amazon-s3.html');
    modalInstance.result.then(function(result) {
      var river = result.river;
      AmazonS3Service.create(river).then(function(result) {
        console.log('Creation result: ' + result);
        notify({
          message: 'River "' + river.name + '" has been created !',
          classes: 'alert-success'
        });
      }).then(function(result) {
        AmazonS3Service.list().then(function(result) {
          $scope.rivers = result;
        });
      });
    });
  };
  
  $scope.editRiver = function(river) {
    var modalInstance = show(river, 'edit-amazon-s3.html');
    modalInstance.result.then(function(result) {
      var river = result.river;
      AmazonS3Service.update(river).then(function(result) {
        console.log('Update result: ' + result);
        notify('River "' + river.name + '" has been updated !');
      });
    });
  };
  
  $scope.removeRiver = function(river) {
    var riverName = river.name;
    AmazonS3Service.delete(river.id).then(function(result) {
      notify({
        message: 'River "' + riverName + '" has been deleted !',
        classes: 'alert-danger'
      });
    }).then(function(result) {
      AmazonS3Service.list().then(function(result) {
        $scope.rivers = result;
      });
    });
  };
  
  function show(river, template) {
    return $modal.open({
      templateUrl: 'views/dialogs/' + template,
      controller: modalController,
      resolve: {
        river: function () {
          return river;
        }
      }
    });
  }
  
  function modalController($scope, $modalInstance, river) {
    $scope.river = river;
    $scope.ok = function(river) {
      $modalInstance.close({
        river: river
      });
    };
    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
  }
  
  });