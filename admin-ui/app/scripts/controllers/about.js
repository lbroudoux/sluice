'use strict';

/**
 * @ngdoc function
 * @name sluiceApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the sluiceApp
 */
angular.module('sluiceApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
